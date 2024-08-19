import React from "react";
import { Map, MapMarker } from "react-kakao-maps-sdk";

export default function KakaoMap({ center }) {
    const defaultCenter = { lat: 33.5563, lng: 126.79581 };

    return (
        <Map
            center={center || defaultCenter}
            style={{ width: "100%", height: "360px" }}
        >
            {center && (
                <MapMarker position={center}></MapMarker>
            )}
        </Map>
    );
}