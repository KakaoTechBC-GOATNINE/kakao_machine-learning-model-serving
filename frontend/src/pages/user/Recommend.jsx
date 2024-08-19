import * as React from 'react';
import Container from "@mui/material/Container";
import LocationFinder from "../../components/user/LocationFinder";

/*
1. 위치 검색 바
2. 현재 위치로 설정
3. 텍스트 입력
4. 검색 버튼
5. 로딩 스피너
 */
export default function Recommend() {
    return (
        <Container component="main" maxWidth="sm">
            <LocationFinder />
        </Container>
    );
}