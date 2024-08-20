from transformers import BertTokenizer, BertForSequenceClassification
import torch
from src.data_processing.api_preprocessing import process_reviews

# 모델 로드
# tokenizer = BertTokenizer.from_pretrained('ilmin/KoBERT_sentiment_v2.03')
# model = BertForSequenceClassification.from_pretrained('ilmin/KoBERT_sentiment_v2.03')

# 직접만든 모델은 아웃풋이 나오지않는 이슈가 있어서 우선 api 연결을 위해 kobert사용
model_name = "monologg/kobert"
tokenizer = BertTokenizer.from_pretrained(model_name)
model = BertForSequenceClassification.from_pretrained(model_name, num_labels=2)

model.eval()

def predict_review_score(review_text: str) -> float:
    try:
        # 텍스트를 전처리하고, 토큰화된 입력 데이터 생성
        inputs = tokenizer(review_text, return_tensors="pt", truncation=True, padding=True)
        # print(f"Tokenized Input: {inputs}")
        
        # 모델 예측 (평가 모드에서)
        with torch.no_grad():
            outputs = model(**inputs)
        # print(f"Model Outputs: {outputs.logits}")
        
        # 소프트맥스를 통해 긍정 및 부정 확률 계산
        probabilities = torch.nn.functional.softmax(outputs.logits, dim=-1)
        negative_score = probabilities[0][0].item()  # 부정 클래스(0)의 확률
        positive_score = probabilities[0][1].item()  # 긍정 클래스(1)의 확률
        
        # 긍정 점수에서 부정 점수를 뺀 결과 계산
        sentiment_score = positive_score - negative_score
        # 재수정 필요함 (모델복구하고 수정)
        sentiment_score = positive_score
        # print(positive_score)
        # print(negative_score)
        # print(f"Sentiment Score: {sentiment_score}")
        
        return sentiment_score
    except Exception as e:
        print(f"Error processing review: {review_text}")
        print(f"Error: {e}")
        return 0.0  # 오류 발생 시 기본값 반환
    
def rank_restaurants(reviews):
    recommendations = []
    
    for store in reviews:
        try:
            store_name, store_score, store_address, store_reviews = store
            # print("-------------------------------")
            # print(f"Processing store: {store_name}")
            
            # 리뷰 텍스트 리스트를 전처리하여 가져옴
            processed_reviews = process_reviews('||'.join(store_reviews))
            total_score = 0.0
            
            for review_text in processed_reviews:
                # print("-------------------------------")
                # print(f"Processing review: {review_text}")
                # 각 리뷰 텍스트에 대해 긍정 점수를 예측
                positive_score = predict_review_score(review_text)
                # print(f"score: {positive_score}")
                total_score += positive_score
            
            # 긍정 확률의 평균 계산
            num_reviews = len(processed_reviews) if len(processed_reviews) > 0 else 1
            avg_positive_score = total_score / num_reviews

            # print(avg_positive_score)
            # print("\n")
            
            recommendations.append({
                "store_name": store_name,
                "address": store_address,
                "positive_score": avg_positive_score,
            })
        except Exception as e:
            print(f"Error processing store: {store_name}")
            print(f"Error: {e}")

    # 가게를 긍정 점수에 따라 내림차순으로 정렬
    ranked_recommendations = sorted(recommendations, key=lambda x: x['positive_score'], reverse=True)
    # print(f"\n\n{ranked_recommendations}\n\n")

    return ranked_recommendations