import React, { useEffect } from 'react';
import axios from 'axios';

function Map({ stores }) {
  useEffect(() => {
    const { kakao } = window;

    const mapContainer = document.getElementById('map');
    if (!mapContainer) {
      console.error("Map container not found!");
      return;
    }

    const mapOption = {
      center: new kakao.maps.LatLng(37.5665, 126.9780), // 초기 중심 위치 (서울시청)
      level: 3, // 지도 확대 수준
    };

    const map = new kakao.maps.Map(mapContainer, mapOption);

    // 주소를 좌표로 변환하는 함수
    const addressToCoord = (address, callback) => {
      axios.get(
        'https://dapi.kakao.com/v2/local/search/address.json?query=' + address,
        {
          headers: {
            Authorization: 'KakaoAK 72a40f848446b331a895daaba6400196',
          },
        }
      )
      .then(response => {
        if (response.data.documents.length > 0) {
          const data = response.data.documents[0];
          const coords = new kakao.maps.LatLng(data.y, data.x);
          callback(coords);
        } else {
          console.error("No coordinates found for address:", address);
        }
      })
      .catch(error => {
        console.error("Error in addressToCoord:", error);
      });
    };

    stores.forEach((store) => {
      addressToCoord(store.address, (coords) => {
        const marker = new kakao.maps.Marker({
          map: map,
          position: coords
        });

        kakao.maps.event.addListener(marker, 'click', function() {
          const infowindow = new kakao.maps.InfoWindow({
            content: `<div style="padding:5px;">${store.store_name}</div>`
          });
          infowindow.open(map, marker);
        });
      });
    });

  }, [stores]);

  return (
    <div>
      <h2>Map View</h2>
      <div id="map" style={{ width: '100%', height: '400px' }}></div>
    </div>
  );
}

export default Map;
