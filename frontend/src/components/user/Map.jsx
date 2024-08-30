import React, { useEffect, useState } from 'react';

function Map({ stores, searchCoords }) {
  const [map, setMap] = useState(null);
  const [markers, setMarkers] = useState([]); // 기존 마커를 추적하는 상태

  // 주소를 좌표로 변환하는 함수 (fetch 사용)
  async function addressToCoords(address) {
    try {
        const response = await fetch(
            `https://dapi.kakao.com/v2/local/search/address.json?query=${address}`,
            {
                headers: {
                    Authorization: 'KakaoAK 72a40f848446b331a895daaba6400196',
                },
            }
        );

        const data = await response.json();
        if (data.documents.length > 0) {
            const document = data.documents[0];
            return { latitude: document.y, longitude: document.x };
        } else {
            throw new Error("No coordinates found for address");
        }
    } catch (error) {
        console.error("Error in addressToCoords:", error);
    }
}

  useEffect(() => {
    // 검색 좌표가 유효한지 확인
    if (searchCoords.latitude == null || searchCoords.longitude == null) {
      console.error("Invalid search coordinates!");
      return;
    }

    // 지도 컨테이너를 찾지 못할 경우 처리
    const mapContainer = document.getElementById('map');
    if (!mapContainer) {
      console.error("Map container not found!");
      return;
    }

    // 지도 옵션 설정
    const mapOption = {
      center: new window.kakao.maps.LatLng(searchCoords.latitude, searchCoords.longitude), // 초기 중심 설정
      level: 3, // 지도 확대 수준
    };

    // 지도 생성
    const createdMap = new window.kakao.maps.Map(mapContainer, mapOption);

    // 줌 컨트롤 추가
    const zoomControl = new window.kakao.maps.ZoomControl();
    createdMap.addControl(zoomControl, window.kakao.maps.ControlPosition.RIGHT);

    setMap(createdMap);  // 생성된 지도를 상태로 저장

    // 현재 위치에 마커 추가 (다른 색상 또는 이미지 사용)
    const currentLocationMarker = new window.kakao.maps.Marker({
      position: new window.kakao.maps.LatLng(searchCoords.latitude, searchCoords.longitude),
      map: createdMap,
      image: new window.kakao.maps.MarkerImage(
        'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png', // 커스텀 이미지 URL
        new window.kakao.maps.Size(24, 35) // 이미지 크기
      )
    });
  }, [searchCoords]);

  useEffect(() => {
    if (map && stores.length > 0) {
      // 기존 마커 제거
      markers.forEach(marker => marker.setMap(null));
      setMarkers([]);

      // 새 마커 추가
      const addMarkers = async () => {
        const newMarkers = await Promise.all(stores.map(async (store) => {
          try {
            const coords = await addressToCoords(store.address);
            if (!coords) {
              console.error(`Invalid coordinates for store: ${store.storeName}`);
              return null;
            }

            // 마커 위치 설정
            const markerPosition = new window.kakao.maps.LatLng(coords.latitude, coords.longitude);
            const marker = new window.kakao.maps.Marker({
              map: map,
              position: markerPosition
            });

            // 마커 클릭 이벤트 - 인포윈도우 추가
            window.kakao.maps.event.addListener(marker, 'click', function () {
              const infowindow = new window.kakao.maps.InfoWindow({
                content: `<div style="padding:5px;">${store.storeName}</div>`,
                removable: true // 인포윈도우 닫기 버튼 추가
              });
              infowindow.open(map, marker);
            });

            return marker;
          } catch (error) {
            console.error(`Failed to add marker for store: ${store.storeName}`, error);
            return null;
          }
        }));

        setMarkers(newMarkers.filter(marker => marker !== null)); // 유효한 마커만 반환
      };

      addMarkers();

    }
  }, [stores, map]);

  return (
    <div style={{ width: '100%', height: '400px', borderRadius: '10px', overflow: 'hidden', boxShadow: '0 2px 10px rgba(0,0,0,0.2)' }}>
      <div id="map" style={{ width: '100%', height: '100%' }}></div>
    </div>
  );
}

export default Map;