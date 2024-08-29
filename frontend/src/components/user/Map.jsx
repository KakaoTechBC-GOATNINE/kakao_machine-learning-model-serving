import React, { useEffect, useState } from 'react';

function Map({ stores, searchCoords }) {
  const [map, setMap] = useState(null);

  useEffect(() => {
    const mapContainer = document.getElementById('map');
    if (!mapContainer) {
      console.error("Map container not found!");
      return;
    }

    const mapOption = {
      center: new window.kakao.maps.LatLng(searchCoords.latitude, searchCoords.longitude), // 초기 중심 설정
      level: 3, // 지도 확대 수준
    };

    const createdMap = new window.kakao.maps.Map(mapContainer, mapOption);
    setMap(createdMap);  // 생성된 지도를 상태로 저장

  }, [searchCoords]); // searchCoords 변경될 때마다 지도 중심을 업데이트

  useEffect(() => {
    if (map) {
      stores.forEach((store) => {
        const coords = new window.kakao.maps.LatLng(store.latitude, store.longitude);
        const marker = new window.kakao.maps.Marker({
          map: map,
          position: coords
        });

        marker.setMap(map);

        window.kakao.maps.event.addListener(marker, 'click', function() {
          const infowindow = new window.kakao.maps.InfoWindow({
            content: `<div style="padding:5px;">${store.store_name}</div>`
          });
          infowindow.open(map, marker);
        });
      });
    }
  }, [stores, map]);

  return (
    <div>
      <h2>여기는 지도</h2>
      <div id="map" style={{ width: '100%', height: '400px' }}></div>
    </div>
  );
}

export default Map;
