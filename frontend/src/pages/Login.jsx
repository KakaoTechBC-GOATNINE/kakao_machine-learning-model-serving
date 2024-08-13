import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';

export default function SignIn() {
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
                <Box component="form" noValidate sx={{ mt: 1 }}>
                    <TextField
                        margin="normal"
                        fullWidth
                        id="loginId"
                        label="ID"
                        name="loginId"
                        autoFocus
                    />
                    <TextField
                        margin="normal"
                        fullWidth
                        name="password"
                        label="PASSWORD"
                        type="password"
                        id="password"
                        autoComplete="current-password"
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
                    >
                        카카오 로그인
                    </Button>
                </Box>
            </Box>
        </Container>
    );
}