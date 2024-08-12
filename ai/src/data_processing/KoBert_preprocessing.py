import os
import pandas as pd

# CSV 파일 경로 설정
input_csv_file_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', '..', 'data', 'raw', 'restaurant_reviews.csv')
output_csv_file_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', '..', 'data', 'processed', 'KoBert_preprocessed_reviews.csv')

# CSV 파일 읽기
df = pd.read_csv(input_csv_file_path, encoding='utf-8-sig')

# 가게 리뷰 부분만 추출
df = df[['Reviews']]

# '레벨' 단어 제거 및 별점 변환 함수
def preprocess_review(review):
    parts = review.split('|')
    
    # 별점 변환 (100% -> 5.0 점수로 변환)
    if len(parts) > 3:
        score_str = parts[3].replace('%', '').replace(';', '').strip()
        try:
            parts[3] = str(float(score_str) / 20)
        except ValueError:
            parts[3] = '0'  # 변환할 수 없는 경우 기본값으로 설정
    
    # 리뷰 텍스트 부분
    review_text = parts[4].strip() if len(parts) > 4 else ''
    
    # 리뷰 텍스트가 비어 있는 경우 None 반환
    if not review_text:
        return None
    
    # parts[2]와 parts[3]을 반환하고, 리뷰 텍스트를 그대로 둠
    return parts[2].strip(), parts[3].strip(), review_text

# 각 리뷰를 전처리하는 함수
def process_reviews(reviews):
    processed_reviews = []
    review_list = reviews.split('||')
    for review in review_list:
        if len(review.split('|')) >= 5:
            processed_review = preprocess_review(review)
            if processed_review:
                processed_reviews.append(processed_review)
    return processed_reviews

# 전처리된 데이터 저장 리스트
final_data = []

# 각 리뷰에 대해 전처리 적용
for reviews in df['Reviews']:
    processed_reviews = process_reviews(reviews)
    if processed_reviews:
        final_data.extend(processed_reviews)

# 전처리된 데이터를 DataFrame으로 변환
processed_df = pd.DataFrame(final_data, columns=['User_Avg_Rating', 'Restaurant_Rating', 'Review_Text'])

# 'User_Avg_Rating'과 'Restaurant_Rating'을 숫자로 변환
processed_df['User_Avg_Rating'] = processed_df['User_Avg_Rating'].astype(float)
processed_df['Restaurant_Rating'] = processed_df['Restaurant_Rating'].astype(float)

# 레이블링 함수 정의
def label_by_absolute_difference(row):
    avg_rating = row['User_Avg_Rating']
    rating_diff = row['Restaurant_Rating'] - avg_rating
    
    # 기준 설정
    if abs(rating_diff) <= avg_rating * 0.05:
        return 2  # 평균
    elif abs(rating_diff) <= avg_rating * 0.10:
        return 3 if rating_diff > 0 else 1  # 좋음 / 나쁨
    else:
        return 4 if rating_diff > 0 else 0  # 아주 좋음 / 아주 나쁨

# 레이블 적용
processed_df['Label'] = processed_df.apply(label_by_absolute_difference, axis=1)

# CSV 파일로 저장
processed_df.to_csv(output_csv_file_path, index=False, encoding='utf-8-sig')

# 결과 출력 (옵션)
print(processed_df.head())

### 라이브러리 설치할거 업데이트 pip3 install konlpy nltk keras
### pip3 install tensorflow??
## pip3 install konlpy jpype1?

# pandas==1.3.3
# numpy==1.21.2
# konlpy==0.5.2
# requests==2.26.0
# jpype1==1.3.0 
# scikit-learn==0.24.2
# matplotlib==3.4.3

# readme
# Java Development Kit (JDK) 1.7 ^