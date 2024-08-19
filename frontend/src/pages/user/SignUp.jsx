import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { useNavigate } from 'react-router-dom';

export default function SignUp() {
    const [serialId, setSerialId] = React.useState('');
    const [password, setPassword] = React.useState('');
    const [nickname, setNickname] = React.useState('');
    const navigate = useNavigate();

    const handleSignUp = async (event) => {
        event.preventDefault(); // 폼 제출 기본 동작 방지

        // 회원가입 데이터를 POST 요청으로 전송
        const response = await fetch('http://localhost:8080/api/v1/auth/basic', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                serialId: serialId,
                password: password,
                nickname: nickname,
            }),
        });

        if (response.ok) {
            alert('회원가입 성공!');
            navigate('/login'); // 회원가입 성공 시 로그인 페이지로 이동
        } else {
            alert('회원가입 실패. 다시 시도해주세요.');
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
                    SIGN UP
                </Typography>
                <Box component="form" noValidate sx={{ mt: 1 }} onSubmit={handleSignUp}>
                    <TextField
                        margin="normal"
                        fullWidth
                        id="serialId"
                        label="ID"
                        name="serialId"
                        autoFocus
                        value={serialId}
                        onChange={(e) => setSerialId(e.target.value)}
                    />
                    <TextField
                        margin="normal"
                        fullWidth
                        name="password"
                        label="PASSWORD"
                        type="password"
                        id="password"
                        autoComplete="new-password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <TextField
                        margin="normal"
                        fullWidth
                        name="nickname"
                        label="NICKNAME"
                        type="text"
                        id="nickname"
                        value={nickname}
                        onChange={(e) => setNickname(e.target.value)}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 2, mb: 2 }}
                    >
                        회원가입
                    </Button>
                </Box>
            </Box>
        </Container>
    );
}
