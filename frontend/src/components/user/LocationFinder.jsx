import Grid from '@mui/material/Unstable_Grid2';
import TextField from "@mui/material/TextField";
import * as React from "react";
import {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import {useDaumPostcodePopup} from "react-daum-postcode";
import GpsFixedIcon from '@mui/icons-material/GpsFixed';
import {IconButton} from "@mui/material";
import KakaoMap from "./KakaoMap";

export default function LocationFinder() {
    const open = useDaumPostcodePopup();
    const [address, setAddress] = useState("");
    const [coords, setCoords] = useState({lat: null, lng: null});
    const [keyword, setKeyword] = useState("");
    const [error, setError] = useState(null);

    const [geocoder, setGeocoder] = useState(null);

    useEffect(() => {
        if (window.kakao && window.kakao.maps) {
            const geocoderInstance = new window.kakao.maps.services.Geocoder();
            setGeocoder(geocoderInstance);
        }
    }, []);

    const searchAddress = () => {
        console.log("1");
        if (coords) {
            geocoder.addressSearch(address, function(result, status) {
                if (status === window.kakao.maps.services.Status.OK) {
                    console.log("2");
                    const { x, y } = result[0];
                    setCoords({ lat: y, lng: x });
                } else {
                    console.log("3");
                    console.error('Geocode failed:', status);
                }
            });
        }

        console.log("4");
        console.log(coords);
    };

    const center = {
        lat : 33.5563, // 기본값을 설정해줄 수 있음
        lng: 126.79581, // 기본값을 설정해줄 수 있음
    };

    const clickCurrentLocationButton = () => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    console.log(position);
                    setCoords({lat: position.latitude, lng: position.longitude});
                    searchAddress();
                    setError(null);
                },
                (error) => {
                    setError(error.message);
                }
            );
        } else {
            setError("Geolocation is not supported by this browser.");
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
        console.log("location: ", address);
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
                <IconButton onClick={clickCurrentLocationButton} aria-label="delete" size="small">
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
            <KakaoMap center={center} />
        </Grid>
    );
};