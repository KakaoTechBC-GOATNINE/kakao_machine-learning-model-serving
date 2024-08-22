import * as React from 'react';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from 'react';
import api from '../Api'; // Axios 인스턴스 불러오기

function getCookie(name) {
    const matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([.$?*|{}()[]\\\/+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function setCookie(name, value, days) {
    const expires = new Date(Date.now() + days * 864e5).toUTCString();
    document.cookie = name + '=' + encodeURIComponent(value) + '; expires=' + expires + '; path=/';
}

function Header() {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [nickname, setNickname] = useState('');

    useEffect(() => {
        const accessToken = getCookie("accessToken");
        const nickname = getCookie("nickname"); // 쿠키에서 nickname 가져오기
        setIsLoggedIn(!!accessToken);
        if (nickname) {
            setNickname(nickname);
        }

        // URL에서 토큰과 닉네임을 받아와 쿠키에 저장하는 로직
        const urlParams = new URLSearchParams(window.location.search);
        const accessTokenFromUrl = urlParams.get('accessToken');
        const nicknameFromUrl = urlParams.get('nickname');

        if (accessTokenFromUrl && nicknameFromUrl) {
            setCookie('accessToken', accessTokenFromUrl, 7);  // 7일 동안 유효
            setCookie('nickname', nicknameFromUrl, 7);
            setIsLoggedIn(true);
            setNickname(nicknameFromUrl);
        }
    }, []);

    const handleLogout = async () => {
        try {
            // 서버에 로그아웃 요청 보내기
            await api.post('/api/v1/logout');

            // 쿠키 제거
            document.cookie = 'accessToken=; Max-Age=0; path=/;';
            document.cookie = 'nickname=; Max-Age=0; path=/;';

            // 로그아웃이 성공하면 UI 상태 업데이트
            setIsLoggedIn(false);
            setNickname('');
            alert("로그아웃 되었습니다.");
            navigate('/'); // 로그아웃 후 홈으로 이동
        } catch (error) {
            console.error("로그아웃 중 오류가 발생했습니다:", error);
            alert("로그아웃 중 오류가 발생했습니다.");
        }
    };

    const navigateTo = (path) => {
        navigate(path);
    };

    return (
        <Toolbar sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <Button size="small" onClick={() => navigateTo('/')}>Home</Button>
            <Button size="small" onClick={() => navigateTo('/qnas')}>Q&A</Button>
            <Typography align="center" sx={{ flex: 1 }} />
            {isLoggedIn ? (
                <>
                    {nickname && (
                        <Typography variant="body1" sx={{ marginRight: '10px' }}>
                            {nickname}
                        </Typography>
                    )}
                    <Button sx={{ ml: 1 }} variant="outlined" size="small" onClick={() => navigateTo('/mypage')}>MyPage</Button>
                    <Button sx={{ ml: 1 }} variant="contained" size="small" onClick={handleLogout}>Logout</Button>
                </>
            ) : (
                <Button sx={{ ml: 1 }} variant="outlined" size="small" onClick={() => navigateTo('/login')}>Login</Button>
            )}
        </Toolbar>
    );
}

export default Header;
