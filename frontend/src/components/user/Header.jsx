import * as React from 'react';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import {useNavigate} from "react-router-dom";

function Header() {
    const nav = useNavigate();

    const navigateTo = (path) => {
        nav(path);
    }

    return (
            <Toolbar sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Button size="small" onClick={() => navigateTo('/')}>Home</Button>
                <Button size="small" onClick={() => navigateTo('/recommend')}>Recommend</Button>
                <Button size="small" onClick={() => navigateTo('/qnas')}>Q&A</Button>

                <Typography align="center" sx={{ flex: 1 }}/>
                <Button sx={{ml: 1}} variant="outlined" size="small" onClick={() => navigateTo('/mypage')}>MyPage</Button>
                <Button sx={{ml: 1}} variant="outlined" size="small" onClick={() => navigateTo('/login')}>login</Button>
                <Button sx={{ml: 1}} variant="contained" size="small">logout</Button>
            </Toolbar>
    );
}

export default Header;