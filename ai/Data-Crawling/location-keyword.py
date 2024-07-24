from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import requests

app = FastAPI()  # FastAPI 애플리케이션 생성

# 카카오 API URL과 API 키 설정
KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json"
KAKAO_API_KEY = "330cca7280154dc662775507f3b51ac4"  # 발급받은 카카오 REST API 키

# 요청 본문 데이터 모델 정의
class KeywordLocationRequest(BaseModel):
    latitude: float  # 위도
    longitude: float  # 경도
    keyword: str  # 사용자가 검색하는 키워드

# 좌표를 주소로 변환하는 함수
def get_location_name(latitude: float, longitude: float) -> str:
    headers = {
        'Authorization': f'KakaoAK {KAKAO_API_KEY}'  # 카카오 API 인증 헤더
    }
    params = {
        'x': longitude,
        'y': latitude
    }
    
    # 카카오 API에 요청을 보내고 응답을 받음
    response = requests.get(KAKAO_API_URL, headers=headers, params=params)
    
    # 응답이 성공적일 경우
    if response.status_code == 200:
        data = response.json()
        # 'documents' 필드가 존재하고 데이터가 있을 경우
        if 'documents' in data and data['documents']:
            address = data['documents'][0]['address']['address_name']
            return address
        else:
            raise HTTPException(status_code=404, detail="Address not found in the documents")
    else:
        raise HTTPException(status_code=response.status_code, detail="Error from Kakao API")

# 주소에서 동네 이름을 추출하는 함수
def extract_dong_name(address: str) -> str:
    parts = address.split()
    # 주소의 세 번째 부분을 동네 이름으로 가정
    if len(parts) >= 3:
        return parts[2]
    return address

# 엔드포인트 정의: POST /api/v1/stores/ai
@app.post("/api/v1/stores/ai")
def extract_keyword_and_dong(request: KeywordLocationRequest):
    try:
        # 위치 좌표로부터 주소를 가져옴
        address = get_location_name(request.latitude, request.longitude)
        # 주소에서 동네 이름을 추출
        dong_name = extract_dong_name(address)
        # 동네 이름과 키워드를 결합한 결과 생성
        result = f"{dong_name} {request.keyword}"
        return {"status": "success", "result": result}
    except HTTPException as e:
        # 예외 발생 시 오류 메시지와 상태 코드 반환
        return {"status": "error", "detail": e.detail}, e.status_code

# 실행 명령어: uvicorn location-keyword:app --reload