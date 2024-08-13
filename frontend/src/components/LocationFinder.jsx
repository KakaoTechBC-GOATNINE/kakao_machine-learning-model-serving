import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import * as React from "react";
import {Stack} from "@mui/material";
import {useState} from "react";

export default function LocationFinder() {

    const [location, setLocation] = useState({ latitude: null, longitude: null });
    const [error, setError] = useState(null);

    const [keyword, setKeyword] = useState("");

    const onChangeKeyword = (e) => {
        setKeyword(e.target.value);
    };

    const findLocation = () => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    setLocation({
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude,
                    });
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

    const recommendApi = () => {
        findLocation();
        console.log("location.latitude: ", location.latitude);
        console.log("location.longitude: ", location.longitude);
        console.log("keyword: ", keyword);
    };

    return (
        <Stack
            direction={{ xs: 'column', sm: 'row' }}
            alignSelf="center"
            spacing={1}
            useFlexGap
            sx={{ pt: 2, width: { xs: '100%', sm: '100%' } }}
        >
            <TextField
                id="outlined-basic"
                value={keyword}
                hiddenLabel
                onChange={onChangeKeyword}
                size="small"
                variant="outlined"
                aria-label="Enter keyword"
                placeholder="검색할 키워드를 입력해주세요."
                inputProps={{
                    autoComplete: 'off',
                    'aria-label': 'Enter your email address',
                }}
                sx={{width: {sm: '80%'}}}
            />
            <Button variant="contained" color="primary" sx={{width: {sm: '20%'}}}
            onClick={recommendApi}>
                검색
            </Button>
        </Stack>
    );
};