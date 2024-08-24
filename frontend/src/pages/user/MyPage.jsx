import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../components/Api';
import { FaEdit } from 'react-icons/fa';
import Alert from "@mui/material/Alert";

const MyPage = () => {
    const [userInfo, setUserInfo] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null); // 서버 통신 및 기타 에러 상태
    const [isEditing, setIsEditing] = useState(false);
    const [nickname, setNickname] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const response = await api.get('/api/v1/users');
                setUserInfo(response.data.data);
                setNickname(response.data.data.nickname);
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

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const handleSaveClick = async () => {
        if (nickname.trim() === '') {
            setError('닉네임을 입력해 주세요.');
            return;
        }

        try {
            await api.post('/api/v1/auth/update', { nickname });
            setIsEditing(false);
            setError(null);
            alert('내 정보가 변경되었습니다.');
        } catch (err) {
            setError('Failed to update nickname.');
        }
    };

    if (loading) {
        return <div style={styles.loading}>Loading...</div>;
    }

    return (
        <div style={styles.container}>
            {error && (
                <Alert severity="error" style={styles.validationError}>{error}</Alert>
            )}
            <h1 style={styles.title}>My Page</h1>
            {userInfo ? (
                <>
                    <div style={styles.userInfo}>
                        <div style={styles.userInfoItem}>
                            <strong style={styles.strong}>ID:</strong>
                            <span style={styles.userInfoText}>{userInfo.serialId}</span>
                        </div>
                        <div style={styles.userInfoItem}>
                            <strong style={styles.strong}>Nickname:</strong>
                            {isEditing ? (
                                <>
                                    <input
                                        type="text"
                                        value={nickname}
                                        onChange={(e) => setNickname(e.target.value)}
                                        style={styles.input}
                                    />
                                </>
                            ) : (
                                <span style={styles.userInfoText}>
                                    {nickname}
                                    <FaEdit style={styles.editIcon} onClick={handleEditClick} />
                                </span>
                            )}
                        </div>
                        <div style={styles.userInfoItem}>
                            <strong style={styles.strong}>등급:</strong>
                            <span style={styles.userInfoText}>{userInfo.eRole === 'ADMIN' ? '관리자' : '회원'}</span>
                        </div>
                        <div style={styles.userInfoItem}>
                            <strong style={styles.strong}>로그인:</strong>
                            <span style={styles.userInfoText}>{userInfo.eProvider === 'KAKAO' ? '카카오' : '일반'}</span>
                        </div>
                    </div>
                    {isEditing && (
                        <div style={styles.buttonContainer}>
                            <button style={styles.saveButton} onClick={handleSaveClick}>
                                저장하기
                            </button>
                        </div>
                    )}
                </>
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
        backgroundColor: '#f9f9f9',
        padding: '20px',
        borderRadius: '8px',
        boxShadow: 'inset 0 0 10px rgba(0, 0, 0, 0.05)',
    },
    userInfoItem: {
        marginBottom: '15px',
        display: 'flex',
        alignItems: 'center',
    },
    userInfoText: {
        marginLeft: '10px',
        fontSize: '1.2em',
        color: '#555',
    },
    strong: {
        color: '#4A90E2',
    },
    input: {
        marginLeft: '10px',
        padding: '5px',
        fontSize: '1.1em',
        borderRadius: '5px',
        border: '1px solid #ccc',
        width: '70%',
    },
    editIcon: {
        marginLeft: '10px',
        cursor: 'pointer',
        color: '#4A90E2',
    },
    validationError: {
        marginBottom: '20px',
    },
    buttonContainer: {
        display: 'flex',
        justifyContent: 'center',
        marginTop: '20px',
    },
    saveButton: {
        padding: '10px 20px',
        fontSize: '1.1em',
        color: '#fff',
        backgroundColor: '#4A90E2',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
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
