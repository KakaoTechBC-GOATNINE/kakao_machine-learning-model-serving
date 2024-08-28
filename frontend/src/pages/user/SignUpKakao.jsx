import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { useNavigate } from 'react-router-dom';
import api from '../../components/Api'; // api 파일을 불러옵니다.

function setCookie(name, value, days) {
    const expires = new Date(Date.now() + days * 864e5).toUTCString();
    document.cookie = name + '=' + encodeURIComponent(value) + '; expires=' + expires + '; path=/';
}

export default function SocialSignUp() {
    const [nickname, setNickname] = React.useState('');
    const [error, setError] = React.useState(null);
    const navigate = useNavigate();

    const handleSaveClick = async () => {
        if (nickname.trim() === '') {
            setError('닉네임을 입력해 주세요.');
            return;
        }

        try {
            await api.post('/api/v1/auth/update', { nickname });
            setError(null);
            setCookie('nickname', nickname);
            alert('닉네임이 등록되었습니다.');
            navigate('/'); // 등록 후 홈 페이지로 이동
            window.location.reload();
        } catch (err) {
            setError('닉네임 등록에 실패했습니다. 다시 시도해 주세요.');
        }
    };

    return (
        <Container component="main" maxWidth="xs">
            <Box
                sx={{
                    marginTop: 8,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}
            >
                <Typography component="h1" variant="h5">
                    닉네임 등록
                </Typography>
                <Box component="form" noValidate sx={{ mt: 1 }}>
                    <TextField
                        margin="normal"
                        fullWidth
                        name="nickname"
                        label="NICKNAME"
                        type="text"
                        id="nickname"
                        value={nickname}
                        onChange={(e) => setNickname(e.target.value)}
                        autoFocus
                    />
                    {error && (
                        <Typography variant="body2" color="error" sx={{ mt: 1 }}>
                            {error}
                        </Typography>
                    )}
                    <Button
                        type="button"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 2, mb: 2 }}
                        onClick={handleSaveClick}
                    >
                        등록
                    </Button>
                </Box>
            </Box>
        </Container>
    );
}
