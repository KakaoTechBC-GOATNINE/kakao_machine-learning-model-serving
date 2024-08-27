import math, re
from src.data_processing.api_preprocessing import process_reviews
from src.api.KcELENTRA_runner import KcELECTRA_predict_review_score
from src.api.BiLSTM_runner import BiLSTM_predict_review_score

def rank_restaurants(reviews):
    recommendations = []
    electra_weight = 0.85
    bilstm_weight = 0.15
    
    for store in reviews:
        try:
            store_name, store_score, store_address, store_reviews = store
            # print("-------------------------------")
            # print(f"Processing store: {store_name}")
            
            # 리뷰 텍스트 리스트를 전처리하여 가져옴
            processed_reviews = process_reviews('||'.join(store_reviews))
            total_score_electra = 0.0
            num_reviews = len(processed_reviews)

            # KcELECTRA 모델을 사용하여 각 리뷰의 점수를 예측
            for review_text in processed_reviews:
                # print("-------------------------------")
                # print(f"Processing review: {review_text}")
                review_score = KcELECTRA_predict_review_score(review_text)
                # print(f"score: {review_score}")
                total_score_electra += review_score
            
            # KcELECTRA 평균 점수 계산
            avg_positive_score_electra = total_score_electra / num_reviews if num_reviews > 0 else 0

            # BiLSTM 모델을 사용하여 모든 리뷰를 하나의 문자열로 결합하여 점수 예측
            combined_reviews = ' '.join(processed_reviews)
            avg_positive_score_bilstm = BiLSTM_predict_review_score(combined_reviews)

            # 리뷰 수에 따른 가중치 계산 (예: log 스케일로 신뢰도를 증가시킴)
            weight = math.log(1 + num_reviews)  # log 스케일을 이용하여 가중치 계산
            
            # 최종 신뢰도 반영 점수 계산
            weighted_score = (electra_weight * avg_positive_score_electra + bilstm_weight * avg_positive_score_bilstm) * weight

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



# 키워드기반으로 랭킹화하는 함수 - 개선필요함
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