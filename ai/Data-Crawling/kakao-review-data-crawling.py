## Issues -> 서비스배포하려면 개선해야함
## 1. 크롤링속도가 너무느림 (슬립줄이고 병렬로 크롤링가능?)
## -> time.sleep 호출을 최소화하고 WebDriverWait를 사용하여 요소가 로드될 때까지 기다림 
## -> ThreadPoolExecutor, 여러 페이지를 병렬로 크롤링
## 30개 식당 크롤링하는대 대략 60초 90초 기존 방식 -> 120초 

#병렬 크롤링 미스된부분 고치고 

# !타임슬립 다 교체하기 WebDriverWait

## 2. 리뷰 페이지수가 적을때도 있음 (현재는 crawl_restaurant_reviews 여기서 인스턴스로 직접 수정해야함) / 또는 아예 없는경우
## -> get_total_pages(driver), 리뷰가 아예없는 경우와 그렇지않은 경우들로 나누어 크롤링, 크롤링속도이슈로 인한 맥시멈 크롤링 페이지는 5로 설정

## 3. 모든 리뷰를 긁어오는것이 아닌 처음에 보이는 3개의 리뷰만 긁어옴 -> 후기더보기 눌러서 다른 것들도 긁어와야함 (후기 더보기가 없는곳도 존재함)
## -> extract_reviews(driver): extract_restaurant_info(driver):, 리뷰 전부 긁어올수 있도록 수정함/ 후기 없는것은 빈 리스트 리턴
## !추가이슈, 페이지 한개에서만 긁어오는걸 해도 소요시간 약 5분소요   
## 병렬크롤링 누락된부분 추가함 -> 시간 모든 페이지 긁어오는데 1분 
## !후기의 개수가 첫번째 페이지에 3개? 즉 후기더보기 버튼이 없을정도로 적을때 에러발생
## !link_close라는 것이 생길때 후기더보기 버튼 안눌림 - 메뉴가 있을경우였음 - 즉 여러변수가생김

## 4. 카카오 맵 자체에 데이터가 그리 많지않음 (식당수가 비교적 적음)

## 5. 검색키워드 나중에 카카오 api로 받아서 진행하게끔 수정해야함

import time, csv, warnings
from concurrent.futures import ThreadPoolExecutor, as_completed
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup

warnings.filterwarnings('ignore')

def setup_driver():
    """크롬 드라이버를 설정하고 반환합니다."""
    options = webdriver.ChromeOptions()
    options.add_argument("--headless")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")
    driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=options)
    return driver

def search_location(driver, location):
    """카카오 맵에서 특정 위치를 검색합니다."""
    url = "https://map.kakao.com/"
    driver.get(url)
    search_area = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, '//*[@id="search.keyword.query"]'))
    )
    search_area.send_keys(location)
    driver.find_element(By.XPATH, '//*[@id="search.keyword.submit"]').send_keys(Keys.ENTER)
    WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, '//*[@id="info.main.options"]/li[2]/a'))
    )
    # 가려진 요소를 숨기기
    driver.execute_script("document.getElementById('dimmedLayer').style.display = 'none';")
    # 요소가 클릭 가능할 때까지 기다림
    WebDriverWait(driver, 10).until(
        EC.element_to_be_clickable((By.XPATH, '//*[@id="info.main.options"]/li[2]/a'))
    ).click()

def extract_reviews(driver):
    """음식점의 리뷰를 추출합니다."""
    reviews = []
    while True:
        try:
            # 페이지 소스를 얻어와서 파싱
            html = driver.page_source
            soup = BeautifulSoup(html, 'html.parser')
            review_elements = soup.select('.list_evaluation > li')
            new_reviews = [review.select('.txt_comment > span')[0].text for review in review_elements if review.select('.txt_comment > span')]
            if not new_reviews:
                reviews.extend(new_reviews)
                break
            
            # 후기 더보기 버튼이 존재하는지 확인
            more_reviews_button = soup.select_one('span:contains("후기 더보기")')
            
            # 더보기 버튼이 존재하지 않으면 루프 종료
            if not more_reviews_button:
                reviews.extend(new_reviews)
                break

            # 후기 더보기 버튼을 기다림
            more_reviews_button = WebDriverWait(driver, 10).until(
                EC.element_to_be_clickable((By.XPATH, '//span[contains(text(), "후기 더보기")]'))
            )
            
            # 버튼 클릭 및 페이지 갱신을 기다림
            more_reviews_button.click()
            time.sleep(0.5)

        except Exception as e:
            reviews.extend(new_reviews)
            print(f"Exception while clicking more reviews button: {e}")
            break
    
    # 후기가 없을 경우, 빈 리스트
    if not reviews:
        reviews.append(' ')

    driver.switch_to.window(driver.window_handles[0])
    WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.TAG_NAME, 'body')))
    return reviews

def extract_restaurant_info(driver, location, page_number):
    """음식점의 정보를 추출하고 리뷰를 추가합니다."""
    search_location(driver, location)

    # 해당 페이지로 이동
    if page_number > 1:
        try:
            xpath = f'/html/body/div[5]/div[2]/div[1]/div[7]/div[6]/div/a[{page_number}]'
            page_button = WebDriverWait(driver, 10).until(
                EC.element_to_be_clickable((By.XPATH, xpath))
            )
            driver.execute_script("arguments[0].click();", page_button)
            time.sleep(1)  # 페이지 로드 대기
        except Exception as e:
            print(f"Error navigating to page {page_number}: {e}")
            return []

    time.sleep(0.2)
    html = driver.page_source
    soup = BeautifulSoup(html, 'html.parser')
    restaurant_elements = soup.select('.placelist > .PlaceItem')
    restaurant_list = []

    for i, restaurant in enumerate(restaurant_elements):
        name = restaurant.select('.head_item > .tit_name > .link_name')[0].text
        score = restaurant.select('.rating > .score > em')[0].text
        addr = restaurant.select('.addr > p')[0].text
        more_reviews_button = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.XPATH, f'//*[@id="info.search.place.list"]/li[{i+1}]/div[5]/div[4]/a[1]'))
        )
        driver.execute_script("arguments[0].click();", more_reviews_button)
        driver.switch_to.window(driver.window_handles[-1])
        time.sleep(1)
        reviews = extract_reviews(driver)
        restaurant_list.append([name, score, addr[3:], reviews])

    driver.quit()
    return restaurant_list

def get_total_pages(driver):
    """총 페이지 수를 반환합니다."""
    try:
        # '검색 결과 없음' 메시지가 있는지 확인
        no_result = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, 'div#info\\.noPlace'))
        )
        time.sleep(0.1)
        if no_result.is_displayed():
            return 0 # pages =0 
    except Exception as e:
        print(f"Error on get_total_pages/noPlace: {e}")
    
    try:
        # 기본 페이지 계산 로직
        page_elements = WebDriverWait(driver, 10).until(
            EC.presence_of_all_elements_located((By.CSS_SELECTOR, 'div#info\\.search\\.page div.pageWrap > a'))
        )
        time.sleep(0.1)

        # 'HIDDEN' 클래스를 가진 요소를 제외한 페이지 수 계산
        pages = len([page for page in page_elements if 'HIDDEN' not in page.get_attribute('class')])
    except Exception as e:
        print(f"Error on get_total_pages: {e}")

    return pages

def crawl_restaurant_reviews(location, pages):
    """특정 위치에서 여러 페이지에 걸쳐 음식점 리뷰를 크롤링합니다."""
    driver = setup_driver()
    search_location(driver, location)
    total_pages = get_total_pages(driver)
    pages = min(pages, total_pages)
    driver.quit()

    all_restaurants = []

    # ThreadPoolExecutor를 사용하여 병렬로 페이지를 크롤링
    with ThreadPoolExecutor(max_workers=5) as executor:
        # 각 페이지에 대해 병렬 작업을 제출
        futures = [executor.submit(extract_restaurant_info, setup_driver(), location, page) for page in range(1, pages + 1)]
        # 작업이 완료될 때까지 기다리고 결과를 수집
        for future in as_completed(futures):
            try:
                all_restaurants.extend(future.result())
            except Exception as e:
                print(f"Error extracting restaurant info: {e}")

    print('\n크롤링 완료')
    return all_restaurants

if __name__ == "__main__":
    location = '판교 이자카야'
    restaurant_reviews = crawl_restaurant_reviews(location, pages=5)  # 최대 5페이지 크롤링
    for restaurant in restaurant_reviews:
        print(restaurant)