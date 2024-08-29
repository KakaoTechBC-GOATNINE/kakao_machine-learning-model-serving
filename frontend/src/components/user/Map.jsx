import React, { useEffect, useState } from 'react';

function Map({ stores, searchCoords }) {
  const [map, setMap] = useState(null);
  const [markers, setMarkers] = useState([]); // 기존 마커를 추적하는 상태

  useEffect(() => {
    const mapContainer = document.getElementById('map');
    if (!mapContainer) {
      console.error("Map container not found!");
      return;
    }

    const mapOption = {
      center: new window.kakao.maps.LatLng(searchCoords.latitude, searchCoords.longitude), // 초기 중심 설정
      level: 3, // 지도 확대 수준
      draggable: true, // 드래그 가능하도록 설정
      scrollwheel: true, // 마우스 휠로 확대/축소 가능
    };

    const createdMap = new window.kakao.maps.Map(mapContainer, mapOption);

    // 줌 컨트롤 추가
    const zoomControl = new window.kakao.maps.ZoomControl();
    createdMap.addControl(zoomControl, window.kakao.maps.ControlPosition.RIGHT);

    setMap(createdMap);  // 생성된 지도를 상태로 저장

  }, [searchCoords]);

  useEffect(() => {
    if (map) {
      // 기존 마커 제거
      markers.forEach(marker => marker.setMap(null));
      setMarkers([]);
  
      // 새 마커 추가
      const newMarkers = stores.map((store) => {
        const coords = new window.kakao.maps.LatLng(store.latitude, store.longitude);
        const marker = new window.kakao.maps.Marker({
          map: map,
          position: coords
        });
  
        // 인포윈도우 추가
        window.kakao.maps.event.addListener(marker, 'click', function() {
          const infowindow = new window.kakao.maps.InfoWindow({
            content: `<div style="padding:5px;">${store.storeName}</div>`
          });
          infowindow.open(map, marker);
        });
  
        return marker;
      });
  
      setMarkers(newMarkers); // 새 마커 목록을 상태로 저장
    }
  }, [stores, map]);

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column', padding: '20px', backgroundColor: '#f4f4f4', borderRadius: '10px', boxShadow: '0 2px 10px rgba(0,0,0,0.1)' }}>
      <h2 style={{ color: '#333', marginBottom: '15px' }}>Map</h2>
      <div id="map" style={{ width: '100%', height: '400px', borderRadius: '10px', overflow: 'hidden', boxShadow: '0 2px 10px rgba(0,0,0,0.2)' }}></div>
    </div>
  );
}

export default Map;
