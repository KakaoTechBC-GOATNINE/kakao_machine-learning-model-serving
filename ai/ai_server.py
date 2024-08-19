from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from src.data_processing.location_keyword import get_location_name, extract_dong_name
from src.data_processing.kakao_review_data_crawling import crawl_restaurant_reviews, save_to_csv

app = FastAPI() # FastAPI 애플리케이션 생성
# 요청 본문 데이터 모델 정의
class KeywordLocationRequest(BaseModel):
    latitude: float  # 위도
    longitude: float  # 경도
    keyword: str  # 사용자가 검색하는 키워드

# 함수이름도 변경
@app.post("/api/v1/stores/ai")
def extract_keyword_and_dong(request: KeywordLocationRequest):
    try:
        # 위치 이름 가져오기
        address = get_location_name(request.latitude, request.longitude)
        # 동네 이름 추출
        dong_name = extract_dong_name(address)
        # 동네 이름과 키워드 결합
        combined = f"{dong_name} {request.keyword}"
        # 리뷰 데이터 크롤링
        reviews = crawl_restaurant_reviews(combined, pages=5) # 최대 5페이지 크롤링
        # 데이터 저장
        # ?? 이제필요한가? 필요없을지도
        save_to_csv(reviews, 'restaurant_reviews.csv') 


        return {"status": "success", "location": combined, "reviews": reviews}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    
# 서버 실행 명령: uvicorn ai_server:app --reload