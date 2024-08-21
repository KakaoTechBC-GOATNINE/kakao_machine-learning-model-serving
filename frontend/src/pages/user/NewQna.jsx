import Container from "@mui/material/Container";
import Stack from '@mui/material/Stack';
import Button from "@mui/material/Button";
import * as React from 'react';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import TextField from "@mui/material/TextField";
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import LockIcon from '@mui/icons-material/Lock';
import { styled } from "@mui/material";
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Box from "@mui/material/Box";

export default function NewQna() {
    const [category, setCategory] = React.useState('');
    const [title, setTitle] = React.useState('');
    const [content, setContent] = React.useState('');
    const [fileCount, setFileCount] = React.useState(0);
    const [files, setFiles] = React.useState([]);
    const [isBlind, setIsBlind] = React.useState(false);
    const navigate = useNavigate();

    const handleChange = (event) => {
        setCategory(event.target.value);
    };

    const fileChange = (event) => {
        const selectedFiles = event.target.files;
        setFileCount(selectedFiles.length);
        setFiles(selectedFiles);
    };

    const handleBlindChange = (event) => {
        setIsBlind(event.target.checked);
    };

    const handleSubmit = async () => {
        if (!title || !content || !category) {
            alert('모든 필드를 채워주세요.');
            return;
        }

        const formData = new FormData();

        // JSON 데이터를 Blob으로 추가
        const json = JSON.stringify({
            title,
            content,
            category,
            isBlind,
        });
        const jsonBlob = new Blob([json], { type: 'application/json' });
        formData.append('qnaRequestDto', jsonBlob);

        // 모든 파일을 FormData에 추가
        Array.from(files).forEach(file => {
            formData.append('images', file);
        });

        const token = getCookie("accessToken");

        try {
            await axios.post('http://localhost:8080/api/v1/qnas', formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            alert('작성되었습니다.');
            navigate('/qnas');
        } catch (error) {
            console.error('작성 실패:', error);
            alert('작성에 실패했습니다.');
        }
    };

    const VisuallyHiddenInput = styled('input')({
        clip: 'rect(0 0 0 0)',
        clipPath: 'inset(50%)',
        height: 1,
        overflow: 'hidden',
        position: 'absolute',
        bottom: 0,
        left: 0,
        whiteSpace: 'nowrap',
        width: 1,
    });

    return (
        <Container component="main" maxWidth="sm" sx={{ marginTop: 4, marginBottom: 4 }}>
            <Stack spacing={3}>
                <FormControl fullWidth>
                    <FormControlLabel
                        control={
                            <Checkbox checked={isBlind} onChange={handleBlindChange} />
                        }
                        label={
                            <Box sx={{ display: 'flex', alignItems: 'center', color: isBlind ? 'red' : 'inherit' }}>
                                {isBlind && <LockIcon sx={{ marginRight: 1 }} />}
                                <span>비공개</span>
                            </Box>
                        }
                        sx={{ marginBottom: 2 }}
                    />
                    <FormControl fullWidth sx={{ marginTop: 2 }}>
                        <InputLabel id="demo-simple-select-label">유형</InputLabel>
                        <Select
                            labelId="demo-simple-select-label"
                            id="demo-simple-select"
                            value={category}
                            label="유형"
                            onChange={handleChange}
                            sx={{ marginBottom: 2 }}
                        >
                            <MenuItem value={"GENERAL"}>일반 질문</MenuItem>
                            <MenuItem value={"ACCOUNT"}>계정 관련</MenuItem>
                            <MenuItem value={"TECH_SUPPORT"}>기술 지원</MenuItem>
                            <MenuItem value={"OTHER"}>기타</MenuItem>
                        </Select>
                    </FormControl>
                    <TextField
                        id="outlined-basic"
                        label="제목"
                        variant="outlined"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        sx={{ marginBottom: 2 }}
                    />
                    <TextField
                        id="outlined-basic"
                        label="내용"
                        variant="outlined"
                        multiline
                        minRows={10}
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        sx={{ marginBottom: 2 }}
                    />
                    <Button
                        component="label"
                        role={undefined}
                        variant="outlined"
                        tabIndex={-1}
                        startIcon={<CloudUploadIcon />}
                        sx={{ marginBottom: 4 }}
                    >
                        {`첨부파일${fileCount > 0 ? ` (${fileCount}건)` : ''}`}
                        <VisuallyHiddenInput
                            type="file"
                            accept="image/*"
                            multiple  // 여러 파일을 선택 가능하게 함
                            onChange={fileChange}
                        />
                    </Button>
                    <Button
                        variant="contained"
                        size="large"
                        onClick={handleSubmit}
                    >
                        작성하기
                    </Button>
                </FormControl>
            </Stack>
        </Container>
    );
}

// 쿠키에서 토큰을 가져오는 함수
function getCookie(name) {
    const matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([.$?*|{}()[]\\\/+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}
