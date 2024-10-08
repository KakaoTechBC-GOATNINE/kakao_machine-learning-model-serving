import * as React from 'react';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from 'react';
import api from '../Api'; // Axios 인스턴스 불러오기
import StarIcon from '@mui/icons-material/Star'; // 별 모양 아이콘

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
    const [role, setRole] = useState(''); // role 상태 추가

    useEffect(() => {
        const accessToken = getCookie("accessToken");
        const nickname = getCookie("nickname");
        const role = getCookie("role"); // 쿠키에서 role 값 가져오기
        setIsLoggedIn(!!accessToken);
        if (nickname) {
            setNickname(nickname);
        }
        if (role) {
            setRole(role);
        }
    }, []);

    const handleLogout = async () => {
        try {
            // 서버에 로그아웃 요청 보내기
            await api.post('/api/v1/logout');

            // 쿠키 제거
            document.cookie = 'accessToken=; Max-Age=0; path=/;';
            document.cookie = 'nickname=; Max-Age=0; path=/;';
            document.cookie = 'role=; Max-Age=0; path=/;'; // role 쿠키 제거

            // 로그아웃이 성공하면 UI 상태 업데이트
            setIsLoggedIn(false);
            setNickname('');
            setRole('');
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
            {role === 'ADMIN' && (
                <Button size="small" onClick={() => navigateTo('/admin')}>ADMIN</Button>
            )}
            <Typography align="center" sx={{ flex: 1 }} />
            {isLoggedIn ? (
                <>
                    {nickname && (
                        <Typography variant="body1" sx={{ marginRight: '10px', display: 'flex', alignItems: 'center' }}>
                            {role === 'ADMIN' && <StarIcon sx={{ color: 'gold', marginRight: '5px' }} />} {/* ADMIN일 경우 별 아이콘 추가 */}
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
