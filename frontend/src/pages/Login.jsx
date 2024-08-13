import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { useNavigate } from 'react-router-dom';  // 페이지 이동을 위한 훅

export default function SignIn() {
    const [loginId, setLoginId] = React.useState('');
    const [password, setPassword] = React.useState('');
    const navigate = useNavigate();  // 네비게이트 훅 사용

    const handleLogin = async (event) => {
        event.preventDefault(); // 폼 제출 기본 동작 방지
    
        // URL 파라미터로 serialId와 password를 추가
        const queryParams = new URLSearchParams({
            serialId: loginId,
            password: password
        });
    
        // POST 요청을 URL에 쿼리 파라미터를 포함하여 전송
<<<<<<< HEAD
        const response = await fetch(`http://shortood.shop/sign-in?${queryParams.toString()}`, { //로컬에서는 localhost:8080
=======
        const response = await fetch(`http://localhost:8080/sign-in?${queryParams.toString()}`, {
>>>>>>> 6638ad141348d8c206f255bdf030dc0f7a477058
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        });
    
        // 로그인 요청 처리 이후 로직을 추가할 수 있습니다.
        if (response.ok) {
            // 로그인 성공 처리
            alert('로그인 성공!');
            //navigate('/dashboard'); // 예: 대시보드로 리다이렉션
        } else {
            alert('로그인 실패. 다시 시도해주세요.');
        }
    };

    const handleKakaoLogin = () => {
<<<<<<< HEAD
        window.location.href = "http://shortood.shop:8080/oauth2/authorization/kakao"; //로컬에서는 localhost:8080
=======
        window.location.href = "http://localhost:8080/oauth2/authorization/kakao";
>>>>>>> 6638ad141348d8c206f255bdf030dc0f7a477058
    };

    const handleNavigateToSignUp = () => {
        navigate('/sign-up');  // 회원가입 페이지로 이동
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
                    LOGIN
                </Typography>
                <Box component="form" noValidate sx={{ mt: 1 }} onSubmit={handleLogin}>
                    <TextField
                        margin="normal"
                        fullWidth
                        id="loginId"
                        label="ID"
                        name="loginId"
                        autoFocus
                        value={loginId}
                        onChange={(e) => setLoginId(e.target.value)}
                    />
                    <TextField
                        margin="normal"
                        fullWidth
                        name="password"
                        label="PASSWORD"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 2, mb: 2 }}
                    >
                        Login
                    </Button>
                    <Button
                        fullWidth
                        sx={{backgroundColor: 'rgb(254, 229, 2)', color: "black", '&:hover': {backgroundColor: 'rgba(254, 229, 2, 0.8)'}}}
                        startIcon={
                            <Box
                                component="img"
                                src="/kakao-logo.png"
                                sx={{ width: 18, height: 24 }}
                            />
                        }
                        onClick={handleKakaoLogin}
                    >
                        카카오 로그인
                    </Button>
                    <Button
                        fullWidth
                        variant="outlined"
                        sx={{ mt: 2, mb: 2 }}
                        onClick={handleNavigateToSignUp}
                    >
                        회원가입
                    </Button>
                </Box>
            </Box>
        </Container>
    );
}
