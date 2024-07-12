
## Issues -> 서비스배포하려면 개선해야함
## 1. 크롤링속도가 너무느림 (슬립줄이고 병렬로 크롤링가능?)
## 2. 리뷰 페이지수가 적을때도 있음 (현재는 crawl_restaurant_reviews 여기서 인스턴스로 직접 수정해야함)
## 3. 모든 리뷰를 긁어오는것이 아닌 처음에 보이는 3개의 리뷰만 긁어옴 -> 후기더보기 눌러서 다른 것들도 긁어와야함 (후기 더보기가 없는곳도 존재함)
## 4. 카카오 맵 자체에 데이터가 그리 많지않음 
## 검색키워드 나중에 카카오 api로 받아서 진행하게끔 수정해야함

import time
import warnings
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
    search_area = driver.find_element(By.XPATH, r'//*[@id="search.keyword.query"]')
    search_area.send_keys(location)
    driver.find_element(By.XPATH, r'//*[@id="search.keyword.submit"]').send_keys(Keys.ENTER)
    time.sleep(5)
    driver.find_element(By.XPATH, r'//*[@id="info.main.options"]/li[2]/a').send_keys(Keys.ENTER)

def extract_reviews(driver):
    """음식점의 리뷰를 추출합니다."""
    html = driver.page_source
    soup = BeautifulSoup(html, 'html.parser')
    review_elements = soup.select('.list_evaluation > li')
    reviews = [review.select('.txt_comment > span')[0].text for review in review_elements if review.select('.txt_comment > span')]
    if not reviews:
        reviews.append(' ')
    driver.close()
    driver.switch_to.window(driver.window_handles[0])
    time.sleep(2)
    return reviews

def extract_restaurant_info(driver):
    """음식점의 정보를 추출하고 리뷰를 추가합니다."""
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
        time.sleep(2)
        reviews = extract_reviews(driver)
        restaurant_list.append([name, score, addr[3:], reviews])

    return restaurant_list

def crawl_restaurant_reviews(location, pages=3):
    """특정 위치에서 여러 페이지에 걸쳐 음식점 리뷰를 크롤링합니다."""
    driver = setup_driver()
    search_location(driver, location)
    all_restaurants = []

    for i in range(1, pages + 1):
        try:
            if i > 5:
                xpath = f'/html/body/div[5]/div[2]/div[1]/div[7]/div[6]/div/a[{i-5}]'
            else:
                xpath = f'/html/body/div[5]/div[2]/div[1]/div[7]/div[6]/div/a[{i}]'
            page_button = WebDriverWait(driver, 10).until(
                EC.element_to_be_clickable((By.XPATH, xpath))
            )
            driver.execute_script("arguments[0].click();", page_button)
            all_restaurants.extend(extract_restaurant_info(driver))
            if i % 5 == 0:
                next_button = WebDriverWait(driver, 10).until(
                    EC.element_to_be_clickable((By.XPATH, r'//*[@id="info.search.page.next"]'))
                )
                driver.execute_script("arguments[0].click();", next_button)
        except Exception as e:
            print(f"Error on page {i}: {e}")
            break

    driver.quit()
    print('크롤링 완료')
    return all_restaurants

if __name__ == "__main__":
    location = '판교 이자카야'
    restaurant_reviews = crawl_restaurant_reviews(location)
    for restaurant in restaurant_reviews:
        print(restaurant)
