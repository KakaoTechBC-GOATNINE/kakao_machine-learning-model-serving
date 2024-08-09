import pandas as pd
import numpy as np
from konlpy.tag import Okt
import requests
import re
import os

# Okt 객체 생성
okt = Okt()

# 외부 한국어 불용어 리스트 가져오기
url = 'https://gist.githubusercontent.com/spikeekips/40d99e1ddc41b06412a6b913d9c9b5b8/raw/3ec2b98210903bd4c8f5b8481e0a8fd0de5e8fc3/stopwords-ko.txt'
response = requests.get(url)
stopwords = set(response.text.splitlines())

# CSV 파일 경로 설정
input_csv_file_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', '..', 'data', 'raw', 'restaurant_reviews.csv')
output_csv_file_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', '..', 'data', 'processed', 'preprocessed_restaurant_reviews.csv')

# CSV 파일 읽기
df = pd.read_csv(input_csv_file_path, encoding='utf-8-sig')

# 가게 이름과 리뷰 부분만 추출
df = df[['Name', 'Reviews']]

# '레벨' 단어 제거 및 별점 변환 함수
def preprocess_review(review):
    parts = review.split('|')
    # 레벨 제거
    level = parts[0].replace('레벨', '').strip()
    # 별점 변환 (100% -> 5.0 점수로 변환)
    if len(parts) > 3:
        score_str = parts[3].replace('%', '').replace(';', '').strip()
        try:
            parts[3] = str(float(score_str) / 20)
        except ValueError:
            parts[3] = '0'  # 변환할 수 없는 경우 기본값으로 설정
    # 리뷰 텍스트 부분
    review_text = parts[4].strip() if len(parts) > 4 else ''
    return level, parts[1].strip(), parts[2].strip(), parts[3].strip(), review_text

# 형태소 분석 및 불용어 제거
def okt_preprocess(text):
    tokens = okt.morphs(text, stem=True)
    filtered_tokens = [word for word in tokens if word not in stopwords and len(word) > 1]
    # 한국어가 아닌 것들 제거
    filtered_tokens = [word for word in filtered_tokens if re.match("[가-힣]+", word)]
    return ' '.join(filtered_tokens)

# 각 리뷰를 전처리하는 함수
def process_reviews(reviews):
    processed_reviews = []
    review_list = reviews.split('||')
    for review in review_list:
        if len(review.split('|')) >= 5:
            level, count, avg_score, score, review_text = preprocess_review(review)
            processed_review_text = okt_preprocess(review_text)
            processed_reviews.append(f"{level}|{count}|{avg_score}|{score}|{processed_review_text}")
    return '||'.join(processed_reviews)


# # Bag of Words 모델 생성
# tokenizer = Tokenizer()
# tokenizer.fit_on_texts(df['Processed_Reviews'])
# sequences = tokenizer.texts_to_sequences(df['Processed_Reviews'])

# # Word to Index, Index to Word
# word_index = tokenizer.word_index   
# index_word = {v: k for k, v in word_index.items()}

# # 문장 길이 분포와 적절한 최대 문자 길이 지정
# max_length = max(len(seq) for seq in sequences)

# # 최대 문자 길이에 따른 패딩 추가
# padded_sequences = pad_sequences(sequences, maxlen=max_length, padding='post')



# 리뷰 부분 전처리
df['Processed_Reviews'] = df['Reviews'].apply(process_reviews)

# 전처리된 데이터 확인
print(df[['Name', 'Processed_Reviews']].head())

# 전처리된 데이터 저장
processed_data_folder = os.path.dirname(output_csv_file_path)
os.makedirs(processed_data_folder, exist_ok=True)
df.to_csv(output_csv_file_path, index=False, encoding='utf-8-sig')

print(f"전처리된 데이터가 {output_csv_file_path}에 저장되었습니다.")

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