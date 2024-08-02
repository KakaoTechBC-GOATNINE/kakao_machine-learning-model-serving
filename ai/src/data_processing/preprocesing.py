# import pandas as pd
# import numpy as np
# import os

# # 데이터 파일 경로 설정
# input_csv_file_path = os.path.join(os.path.dirname(__file__), '../../data/raw/restaurant_reviews.csv')
# output_csv_file_path = os.path.join(os.path.dirname(__file__), '../../data/processed/preprocessed_restaurant_reviews.csv')

# # CSV 파일 읽기
# df = pd.read_csv(input_csv_file_path, encoding='utf-8-sig')

# # 데이터 프레임의 기본 정보 확인
# print(df.info())
# print(df.head())

# # 예시 데이터 프레임
# # Columns: ['Name', 'Score', 'Address', 'Reviews']

# # 1. 불필요한 열 제거
# df = df.drop(columns=['Address'])

# # 2. 결측값 처리
# df = df.dropna()  # 결측값이 있는 행 제거
# # 또는
# # df = df.fillna('')  # 결측값을 빈 문자열로 채우기

# # 3. 텍스트 데이터 정제
# df['Reviews'] = df['Reviews'].str.lower().str.strip()

# # 전처리된 데이터 확인
# print(df.head())

# # 전처리된 데이터 저장을 위한 폴더 생성
# processed_data_folder = os.path.dirname(output_csv_file_path)
# os.makedirs(processed_data_folder, exist_ok=True)

# # 전처리된 데이터 저장
# df.to_csv(output_csv_file_path, index=False, encoding='utf-8-sig')

# print(f"전처리된 데이터가 {output_csv_file_path}에 저장되었습니다.")


import pandas as pd
import numpy as np
import os
from konlpy.tag import Komoran
import nltk
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences

# nltk에서 한국어 불용어 로드
nltk.download('stopwords')
from nltk.corpus import stopwords
stop_words = set(stopwords.words('korean'))

# Komoran 객체 생성
komoran = Komoran()

# CSV 파일 경로 설정
input_csv_file_path = os.path.join(os.path.dirname(__file__), '../../data/raw/restaurant_reviews.csv')
output_csv_file_path = os.path.join(os.path.dirname(__file__), '../../data/processed/preprocessed_restaurant_reviews.csv')

# CSV 파일 읽기
df = pd.read_csv(input_csv_file_path, encoding='utf-8-sig')

# 데이터 중복 제거
df.drop_duplicates(inplace=True)

# 결측 값 제거
df.dropna(subset=['Reviews'], inplace=True)

# 한글 토크나이저를 활용한 토큰화 (Komoran)
def tokenize(text):
    tokens = komoran.morphs(text)
    return tokens

# 불용어 제거
def remove_stopwords(tokens):
    filtered_tokens = [word for word in tokens if word not in stop_words]
    return filtered_tokens

# 리뷰 텍스트 전처리
df['Processed_Reviews'] = df['Reviews'].apply(lambda x: ' '.join(remove_stopwords(tokenize(x.lower()))))

# Bag of Words 모델 생성
tokenizer = Tokenizer()
tokenizer.fit_on_texts(df['Processed_Reviews'])
sequences = tokenizer.texts_to_sequences(df['Processed_Reviews'])

# Word to Index, Index to Word
word_index = tokenizer.word_index
index_word = {v: k for k, v in word_index.items()}

# 문장 길이 분포와 적절한 최대 문자 길이 지정
max_length = max(len(seq) for seq in sequences)

# 최대 문자 길이에 따른 패딩 추가
padded_sequences = pad_sequences(sequences, maxlen=max_length, padding='post')

# 전처리된 데이터 저장
df['Padded_Sequences'] = list(padded_sequences)
processed_data_folder = os.path.dirname(output_csv_file_path)
os.makedirs(processed_data_folder, exist_ok=True)
df.to_csv(output_csv_file_path, index=False, encoding='utf-8-sig')

print(f"전처리된 데이터가 {output_csv_file_path}에 저장되었습니다.")














### 라이브러리 설치할거 업데이트 pip3 install konlpy nltk keras
### pip3 install tensorflow??
## pip3 install konlpy

