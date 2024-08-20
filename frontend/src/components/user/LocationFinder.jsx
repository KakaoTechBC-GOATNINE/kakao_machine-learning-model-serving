import Grid from '@mui/material/Unstable_Grid2';
import TextField from "@mui/material/TextField";
import * as React from "react";
import {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import {useDaumPostcodePopup} from "react-daum-postcode";
import GpsFixedIcon from '@mui/icons-material/GpsFixed';
import {IconButton} from "@mui/material";
import axios from 'axios';

export default function LocationFinder() {
    const open = useDaumPostcodePopup();
    const [address, setAddress] = useState("");
    const [keyword, setKeyword] = useState("");
    const [error, setError] = useState(null);
    const [coords, setCoords] = useState({latitude: "", longitude: ""});

    const setCoordsAndAddress = (lat, lng) => {
        coordsToAddress(lat, lng);
        setCoords({latitude: lat, longitude: lng});
    };

    function normalizeCoordsToString(coords) {
        return {
            latitude: coords.latitude.toString(),
            longitude: coords.longitude.toString(),
        };
    }

    function addressTocoord(add) {
        axios.get(
            'https://dapi.kakao.com/v2/local/search/address.json?query=' + add,
            {
                headers: {
                    Authorization: 'KakaoAK 72a40f848446b331a895daaba6400196',
                },
            }
        )
        .then(response => {
            const data = response.data.documents[0].road_address;
            setCoords({latitude: data.y, longitude: data.x });
        })
        .catch(error => {
            console.log(error.message);
        });
    }

    
    function coordsToAddress(lat, lng) {
        axios.get(
            'https://dapi.kakao.com/v2/local/geo/coord2address.json?input_coord=WGS84&x=' + lng + '&y=' + lat,
            {
                headers: {
                    Authorization: 'KakaoAK 72a40f848446b331a895daaba6400196',
                },
            }
        )
        .then(response => {
            const location = response.data.documents[0];
            const tempAddress = location.address.region_1depth_name + " " + location.address.region_2depth_name + " " + location.address.region_3depth_name;
            setAddress(tempAddress);
        })
        .catch(error => {
            console.log(error.message);
        });
    }

    const findCurrentCoordsButtonEvent = () => {
        if (navigator.geolocation) {        
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    setCoordsAndAddress(position.coords.latitude, position.coords.longitude);
                    setError(null);
                },
                (error) => {
                    // Todo: 위치 엑서스 허용하지 않을 경우 Alert 출력 안됨. 추후 수정 필요
                    if (error.code === error.PERMISSION_DENIED) {
                        alert("현재 위치 허용 시에만 사용 가능한 기능입니다.");
                    } else {
                        setError(error.message);
                    }
                }
            );
        } else {
            setError("Geolocation is not supported by this browser.");
            alert("현재 위치를 찾지 못했습니다. 잠시 후 다시 시도해주세요.");
        }
    };

    const onChangeKeyword = (e) => {
        setKeyword(e.target.value);
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            recommendApi();
        }
    };

    const recommendApi = () => {
        console.log("keyword: ", keyword);
        console.log("address: ", address);
        console.log("coords: ", normalizeCoordsToString(coords));
    };

    const findButtonClick = () => {
        open({onComplete: findButtonEvent});
    };

    const findButtonEvent = (data) => {
        let fullAddress = data.address;
        let extraAddress = '';

        if (data.addressType === 'R') {
            if (data.bname !== '') {
                extraAddress += data.bname;
            }
            if (data.buildingName !== '') {
                extraAddress += extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName;
            }
            fullAddress += extraAddress !== '' ? ` (${extraAddress})` : '';
        }
        setAddress(fullAddress);
        addressTocoord(fullAddress);
    };
    return (
        <Grid container spacing={2} sx={{marginTop: '30px'}}>
            <Grid xs={8}>
                <TextField
                    InputProps={{
                        readOnly: true,
                    }}
                    id="location"
                    value={address}
                    hiddenLabel
                    size="small"
                    variant="outlined"
                    aria-label="Enter keyword"
                    placeholder="주소 검색 버튼을 클릭하여 주소를 설정해주세요."
                    inputProps={{
                        autoComplete: 'off',
                    }}
                    fullWidth
                    required
                />
            </Grid>
            <Grid xs={1}>
                <IconButton onClick={findCurrentCoordsButtonEvent} aria-label="delete" size="small">
                    <GpsFixedIcon fontSize="small" />
                </IconButton>
            </Grid>
            <Grid xs={3}>
                <Button id="findButton" variant="contained" color="primary" fullWidth
                        onClick={findButtonClick}>
                    주소 검색
                </Button>
            </Grid>
            <Grid xs={12}>
                <TextField
                    id="outlined-basic"
                    value={keyword}
                    hiddenLabel
                    onChange={onChangeKeyword}
                    onKeyPress={handleKeyPress}
                    size="small"
                    variant="outlined"
                    aria-label="Enter keyword"
                    placeholder="검색할 키워드를 입력해주세요. (ex: 양식)"
                    inputProps={{
                        autoComplete: 'off',
                    }}
                    fullWidth
                />
            </Grid>
            <Grid xs={12}>
                <Button variant="contained" color="primary" fullWidth
                        onClick={recommendApi}>
                    검색
                </Button>
            </Grid>
        </Grid>
    );
};