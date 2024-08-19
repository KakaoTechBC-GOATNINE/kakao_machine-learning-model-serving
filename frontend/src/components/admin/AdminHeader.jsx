import * as React from 'react';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import {useNavigate} from "react-router-dom";

function AdminHeader() {
    const nav = useNavigate();

    const navigateTo = (path) => {
        nav(path);
    }

    return (
        <Toolbar sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <Button size="small" onClick={() => navigateTo('/admin')}>admin Home</Button>
            <Button size="small" onClick={() => navigateTo('/admin/users')}>admin users</Button>
            <Button size="small" onClick={() => navigateTo('/admin/qnas')}>Q&A</Button>

            <Typography align="center" sx={{ flex: 1 }}/>
            <Button sx={{ml: 1}} variant="outlined" size="small" onClick={() => navigateTo('/login')}>login</Button>
            <Button sx={{ml: 1}} variant="contained" size="small">logout</Button>
        </Toolbar>
    );
}

export default AdminHeader;