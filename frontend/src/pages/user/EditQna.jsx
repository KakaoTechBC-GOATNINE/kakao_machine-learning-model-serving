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
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import CircularProgress from "@mui/material/CircularProgress";
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';

export default function EditQna() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [category, setCategory] = React.useState('');
    const [title, setTitle] = React.useState('');
    const [content, setContent] = React.useState('');
    const [isBlind, setIsBlind] = React.useState(false);
    const [files, setFiles] = React.useState([]);
    const [existingFiles, setExistingFiles] = React.useState([]);
    const [loading, setLoading] = React.useState(true);

    React.useEffect(() => {
        const fetchData = async () => {
            const token = getCookie("accessToken");

            try {
                const response = await axios.get(`${process.env.REACT_APP_BASE_URL}/api/v1/qnas/${id}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                const data = response.data;
                setCategory(data.category);
                setTitle(data.title);
                setContent(data.content);
                setIsBlind(data.isBlind);

                const imagePromises = data.images.map(async (image) => {
                    const imageResponse = await axios.get(`${process.env.REACT_APP_BASE_URL}/api/v1/qnas/image/${image.uuidName}`, {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                        responseType: 'blob',
                    });
                    const file = new File([imageResponse.data], image.originName, {
                        type: imageResponse.data.type,
                    });
                    return file;
                });

                const filesArray = await Promise.all(imagePromises);
                setFiles(filesArray);

                const urls = filesArray.map(file => ({
                    url: URL.createObjectURL(file),
                    originName: file.name,
                    file: file
                }));
                setExistingFiles(urls);
                setLoading(false);
            } catch (error) {
                console.error("Failed to load Q&A data:", error);
                alert("Q&A 데이터를 불러오는데 실패했습니다.");
                navigate('/qnas');
            }
        };

        fetchData();
    }, [id, navigate]);

    const handleChange = (event) => {
        setCategory(event.target.value);
    };

    const fileChange = (event) => {
        const selectedFiles = Array.from(event.target.files);
        const updatedFiles = [...files, ...selectedFiles];
        setFiles(updatedFiles);

        const newPreviews = selectedFiles.map(file => ({
            url: URL.createObjectURL(file),
            originName: file.name,
            file: file
        }));

        setExistingFiles(prev => [...prev, ...newPreviews]);
    };

    const handleBlindChange = (event) => {
        setIsBlind(event.target.checked);
    };

    const handleRemoveFile = (index) => {
        setFiles(prevFiles => prevFiles.filter((_, i) => i !== index));
        setExistingFiles(prevPreviews => prevPreviews.filter((_, i) => i !== index));
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

        files.forEach(file => {
            formData.append('images', file);
        });

        const token = getCookie("accessToken");

        try {
            await axios.put(`${process.env.REACT_APP_BASE_URL}/api/v1/qnas/${id}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${token}`,
                },
            });
            alert('수정되었습니다.');
            navigate('/qnas');
        } catch (error) {
            console.error('수정 실패:', error);
            alert('수정에 실패했습니다.');
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

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <CircularProgress />
            </Box>
        );
    }

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
                    <Box sx={{ marginBottom: 2 }}>
                        {existingFiles.map((file, index) => (
                            <Box key={index} sx={{ display: 'flex', alignItems: 'center', marginBottom: 1 }}>
                                <img
                                    src={file.url}
                                    alt={file.originName}
                                    style={{ maxWidth: '50px', marginRight: '8px' }}
                                />
                                <Typography variant="body2">{file.originName}</Typography>
                                <IconButton
                                    onClick={() => handleRemoveFile(index)}
                                    size="small"
                                    sx={{ marginLeft: 1 }}
                                >
                                    <CloseIcon fontSize="small" />
                                </IconButton>
                            </Box>
                        ))}
                    </Box>
                    <Button
                        component="label"
                        role={undefined}
                        variant="outlined"
                        tabIndex={-1}
                        startIcon={<CloudUploadIcon />}
                        sx={{ marginBottom: 4 }}
                    >
                        {`첨부파일${files.length > 0 ? ` (${files.length}건)` : ''}`}
                        <VisuallyHiddenInput
                            type="file"
                            accept="image/*"
                            multiple
                            onChange={fileChange}
                        />
                    </Button>
                    <Button
                        variant="contained"
                        size="large"
                        onClick={handleSubmit}
                    >
                        수정하기
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
