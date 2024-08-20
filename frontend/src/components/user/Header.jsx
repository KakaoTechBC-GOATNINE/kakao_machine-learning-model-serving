import * as React from 'react';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from 'react';

function getCookie(name) {
    const matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([.$?*|{}()[]\\\/+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
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
    }, []);

    const handleLogout = () => {
        document.cookie = 'accessToken=; Max-Age=0; path=/;';
        document.cookie = 'refreshToken=; Max-Age=0; path=/;';
        setIsLoggedIn(false);
        setNickname(''); // 로그아웃 시 닉네임 초기화
        alert("로그아웃 되었습니다.");
        navigate('/'); // 로그아웃 후 홈으로 이동
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
