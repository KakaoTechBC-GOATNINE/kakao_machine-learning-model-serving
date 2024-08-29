import * as React from 'react';
import Container from "@mui/material/Container";
import LocationFinder from "../../components/user/LocationFinder";
import Map from '../../components/user/Map'; // Map 컴포넌트 임포트
import StoreList from '../../components/user/StoreList'; // StoreList 컴포넌트 임포트

export default function Recommend() {
    const [coords, setCoords] = React.useState({ latitude: "", longitude: "" }); // 초기값
    const [stores, setStores] = React.useState([]); // 가게 리스트 상태

    return (
        <div>
            <Container component="main" maxWidth="sm"> {/* LocationFinder는 sm 사이즈로 */}
                <LocationFinder setCoords={setCoords} setStores={setStores}/>
            </Container>

            <Container component="main" maxWidth="lg" sx={{marginTop: "20px"}}>
                <div style={{ display: 'flex', height: '600px' }}>
                    <div style={{ flex: 2, marginRight: '10px' }}>
                        <Map stores={stores} searchCoords={coords} /> {/* 검색 결과 지도 표시 */}
                    </div>
                    <div style={{ flex: 1 }}>
                        <StoreList stores={stores} /> {/* 검색 결과 리스트 */}
                    </div>
                </div>
            </Container>
        </div>
    );
}
