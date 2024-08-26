import * as React from 'react';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { useNavigate } from "react-router-dom";
import api from '../Api'; // Axios 인스턴스 불러오기

function AdminHeader() {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            // 서버에 로그아웃 요청 보내기
            await api.post('/api/v1/logout');

            // 쿠키 제거
            document.cookie = 'accessToken=; Max-Age=0; path=/;';
            document.cookie = 'nickname=; Max-Age=0; path=/;';
            document.cookie = 'role=; Max-Age=0; path=/;'; // role 쿠키 제거

            // 로그아웃 후 홈으로 이동
            alert("로그아웃 되었습니다.");
            navigate('/');
        } catch (error) {
            console.error("로그아웃 중 오류가 발생했습니다:", error);
            alert("로그아웃 중 오류가 발생했습니다.");
        }
    };

    const navigateTo = (path) => {
        navigate(path);
    }

    return (
        <Toolbar sx={{ borderBottom: 1, borderColor: 'divider', backgroundColor: '#333', color: '#fff' }}>
            <Button size="small" onClick={() => navigateTo('/')} sx={{ color: '#fff' }}>
                HOME
            </Button>
            <Button size="small" onClick={() => navigateTo('/admin/qnas')} sx={{ color: '#fff' }}>
                Q&A
            </Button>
            <Button size="small" onClick={() => navigateTo('/admin/users')} sx={{ color: '#fff' }}>
                USERS
            </Button>

            <Typography align="center" sx={{ flex: 1 }} />
            <Button sx={{ ml: 1, backgroundColor: '#ff6347', color: '#fff' }} variant="contained" size="small" onClick={handleLogout}>
                로그아웃
            </Button>
        </Toolbar>
    );
}

export default AdminHeader;
