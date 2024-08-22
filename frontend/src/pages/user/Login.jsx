import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';

export default function SignIn() {
    const [loginId, setLoginId] = React.useState('');
    const [password, setPassword] = React.useState('');

    return (
        <Container component="main" maxWidth="xs">
            <Box sx={{ marginTop: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <Typography component="h1" variant="h5">LOGIN</Typography>
                <Box
                    component="form"
                    noValidate
                    sx={{ mt: 1 }}
                    method="POST"
                    action={`${process.env.REACT_APP_API_BASE_URL}/api/v1/sign-in`}
                >
                    <TextField
                        margin="normal"
                        fullWidth
                        id="loginId"
                        label="ID"
                        name="serialId"
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
                        sx={{ backgroundColor: 'rgb(254, 229, 2)', color: "black", '&:hover': { backgroundColor: 'rgba(254, 229, 2, 0.8)' }}}
                        startIcon={
                            <Box
                                component="img"
                                src="/kakao-logo.png"
                                sx={{ width: 18, height: 24 }}
                            />
                        }
                        onClick={() => { window.location.href = `${process.env.REACT_APP_API_KAKAO_URL}:8080/oauth2/authorization/kakao`; }}
                    >
                        카카오 로그인
                    </Button>
                    <Button
                        fullWidth
                        variant="outlined"
                        sx={{ mt: 2, mb: 2 }}
                        onClick={() => { window.location.href = '/sign-up'; }}
                    >
                        회원가입
                    </Button>
                </Box>
            </Box>
        </Container>
    );
}
