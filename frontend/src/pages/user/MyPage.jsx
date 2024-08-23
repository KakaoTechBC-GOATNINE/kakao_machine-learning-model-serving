import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../components/Api'; // customAxios 파일 경로에 맞게 수정

const MyPage = () => {
    const [userInfo, setUserInfo] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const response = await api.get('/api/v1/users');
                setUserInfo(response.data.data);
                setLoading(false);
            } catch (err) {
                if (err.response && err.response.status === 400) {
                    alert("로그인 페이지로 이동합니다.");
                    navigate('/login');
                } else {
                    setError('Failed to load user information.');
                }
                setLoading(false);
            }
        };

        fetchUserInfo();
    }, [navigate]);

    if (loading) {
        return <div style={styles.loading}>Loading...</div>;
    }

    if (error) {
        return <div style={styles.error}>{error}</div>;
    }

    const roleDisplay = userInfo.eRole === 'ADMIN' ? '관리자' : '회원';
    const providerDisplay = userInfo.eProvider === 'KAKAO' ? '카카오' : '일반';

    return (
        <div style={styles.container}>
            <h1 style={styles.title}>My Page</h1>
            {userInfo ? (
                <div style={styles.userInfo}>
                    <p style={styles.userInfoText}><strong style={styles.strong}>ID:</strong> {userInfo.serialId}</p>
                    <p style={styles.userInfoText}><strong style={styles.strong}>Nickname:</strong> {userInfo.nickname}</p>
                    <p style={styles.userInfoText}><strong style={styles.strong}>등급:</strong> {roleDisplay}</p>
                    <p style={styles.userInfoText}><strong style={styles.strong}>로그인:</strong> {providerDisplay}</p>
                </div>
            ) : (
                <p>No user information available.</p>
            )}
        </div>
    );
};

const styles = {
    container: {
        maxWidth: '500px',
        margin: '50px auto',
        padding: '30px',
        backgroundColor: '#ffffff',
        borderRadius: '10px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        fontFamily: 'Arial, sans-serif',
        color: '#333',
    },
    title: {
        textAlign: 'center',
        fontSize: '2.5em',
        color: '#4A90E2',
        marginBottom: '20px',
    },
    userInfo: {
        marginTop: '20px',
        padding: '20px',
        backgroundColor: '#f9f9f9',
        borderRadius: '8px',
        boxShadow: 'inset 0 0 10px rgba(0, 0, 0, 0.05)',
    },
    userInfoText: {
        fontSize: '1.1em',
        color: '#555',
        lineHeight: '1.8',
        margin: '10px 0',
    },
    strong: {
        color: '#4A90E2',
    },
    loading: {
        textAlign: 'center',
        fontSize: '1.5em',
        color: '#ff6347',
        marginTop: '20px',
    },
    error: {
        textAlign: 'center',
        fontSize: '1.5em',
        color: '#ff6347',
        marginTop: '20px',
    },
};

export default MyPage;
