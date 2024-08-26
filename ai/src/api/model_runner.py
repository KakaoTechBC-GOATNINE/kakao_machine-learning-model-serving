import math, re
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch
from src.data_processing.api_preprocessing import process_reviews

# 모델 로드
model_name = "ilmin/KcELECTRA_sentiment_model_v1.01"
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForSequenceClassification.from_pretrained(model_name, num_labels=3)
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
        negative_score = probabilities[0][0].item()  # 안좋은 리뷰 (0)의 확률
        neutral_score = probabilities[0][1].item()  # 평범한 리뷰 (1)의 확률
        positive_score = probabilities[0][2].item()  # 좋은 리뷰 (2)의 확률
        
        # 감정 점수 계산
        sentiment_score = (-1.5 * negative_score) + (0.5 * neutral_score) + (1.5 * positive_score)
        # print(f"P: {positive_score}")
        # print(f"n: {neutral_score}")
        # print(f"N: {negative_score}")
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
            num_reviews = len(processed_reviews)

            for review_text in processed_reviews:
                # print("-------------------------------")
                # print(f"Processing review: {review_text}")
                # 각 리뷰 텍스트에 대해 리뷰 점수를 예측
                review_score = predict_review_score(review_text)
                # print(f"score: {review_score}")
                total_score += review_score
            
            # 리뷰 점수의 평균 계산
            avg_positive_score = total_score / num_reviews if num_reviews > 0 else 0

            # 리뷰 수에 따른 가중치 계산 (예: log 스케일로 신뢰도를 증가시킴)
            weight = math.log(1 + num_reviews)  # log 스케일을 이용하여 가중치 계산
            
            # 최종 신뢰도 반영 점수 계산
            weighted_score = avg_positive_score * weight

            # print(f"Weighted Score for {store_name}: {weighted_score}")
            # print("\n")
            
            recommendations.append({
                "store_name": store_name,
                "address": store_address,
                "positive_score": weighted_score,
            })
        except Exception as e:
            print(f"Error processing store: {store_name}")
            print(f"Error: {e}")

    # 가게를 신뢰도 반영 점수에 따라 내림차순으로 정렬
    ranked_recommendations = sorted(recommendations, key=lambda x: x['positive_score'], reverse=True)
    # print(f"\n\n{ranked_recommendations}\n\n")

    return ranked_recommendations

# def rank_restaurants_keywords(reviews, target_keywords):
#     recommendations = []
    
#     for store in reviews:
#         try:
#             store_name, store_score, store_address, store_reviews = store
#             print("-------------------------------")
#             print(f"Processing store: {store_name}")
            
#             # 리뷰 텍스트 리스트를 전처리하여 가져옴
#             processed_reviews = process_reviews('||'.join(store_reviews))
#             total_score = 0.0
#             keyword_count = 0
#             num_reviews = len(processed_reviews)

#             for review_text in processed_reviews:
#                 print("-------------------------------")
#                 print(f"Processing review: {review_text}")
#                 # 각 리뷰 텍스트에 대해 리뷰 점수를 예측
#                 review_score = predict_review_score(review_text)
#                 print(f"score: {review_score}")
#                 total_score += review_score
                
#                 # 키워드가 리뷰에 포함되어 있는지 확인
#                 for keyword in target_keywords:
#                     if re.search(keyword, review_text, re.IGNORECASE):
#                         keyword_count += 1
#                         break  # 키워드가 하나라도 있으면 더 이상 체크하지 않음

#             # 리뷰 점수의 평균 계산
#             avg_positive_score = total_score / num_reviews if num_reviews > 0 else 0

#             # 리뷰 수에 따른 가중치 계산 (예: log 스케일로 신뢰도를 증가시킴)
#             review_weight = math.log(1 + num_reviews)
            
#             # 키워드 가중치 계산 (키워드가 포함된 리뷰의 비율)
#             keyword_weight = keyword_count / num_reviews if num_reviews > 0 else 0
            
#             # 최종 점수 계산 (리뷰 점수에 키워드 가중치와 리뷰 수 가중치를 곱함)
#             weighted_score = avg_positive_score * review_weight * (1 + keyword_weight)

#             print(f"Weighted Score for {store_name}: {weighted_score}")
#             print("\n")
            
#             recommendations.append({
#                 "store_name": store_name,
#                 "address": store_address,
#                 "positive_score": weighted_score,
#                 "keyword_mention_count": keyword_count
#             })
#         except Exception as e:
#             print(f"Error processing store: {store_name}")
#             print(f"Error: {e}")

#     # 가게를 신뢰도 반영 점수에 따라 내림차순으로 정렬
#     ranked_recommendations = sorted(recommendations, key=lambda x: x['positive_score'], reverse=True)
#     print(f"\n\n{ranked_recommendations}\n\n")

#     return ranked_recommendations