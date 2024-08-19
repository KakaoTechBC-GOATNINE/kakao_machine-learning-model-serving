import Container from "@mui/material/Container";
import Stack from '@mui/material/Stack';
import Button from "@mui/material/Button";
import * as React from 'react';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import TextField from "@mui/material/TextField";

import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import {styled} from "@mui/material";


// TODO: 이전에 등록한 이미지 삭제 기능 추가
export default function EditQna() {
    const [category, setCategory] = React.useState('');
    const [fileCount, setFileCount] = React.useState(0);

    const handleChange = (event) => {
        setCategory(event.target.value);
    };

    const fileChange = (event) => {
        const files = event.target.files;
        setFileCount(files.length);
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
                    <InputLabel id="demo-simple-select-label">유형</InputLabel>
                    <Select
                        labelId="demo-simple-select-label"
                        id="demo-simple-select"
                        value={category}
                        label="유형"
                        onChange={handleChange}
                        sx={{marginBottom: 2}}
                    >
                        <MenuItem value={"GENERAL"}>일반 질문</MenuItem>
                        <MenuItem value={"ACCOUNT"}>계정 관련</MenuItem>
                        <MenuItem value={"TECH_SUPPORT"}>기술 지원</MenuItem>
                        <MenuItem value={"OTHER"}>기타</MenuItem>
                    </Select>
                    <TextField id="outlined-basic" label="제목" variant="outlined" sx={{marginBottom: 2}} />
                    <TextField
                        id="outlined-basic"
                        label="내용"
                        variant="outlined"
                        multiline
                        minRows={10}
                        sx={{marginBottom: 2}}
                    />
                    <Button
                        component="label"
                        role={undefined}
                        variant="outlined"
                        tabIndex={-1}
                        startIcon={<CloudUploadIcon />}
                        sx={{marginBottom: 4}}
                    >
                        {`첨부파일${fileCount > 0 ? ` (${fileCount}건)` : ''}`}
                        <VisuallyHiddenInput
                            type="file"
                            accept="image/*"
                            multiple
                            onChange={fileChange}
                        />
                    </Button>
                    <Button variant="contained" size="large">수정하기</Button>
                </FormControl>
            </Stack>
        </Container>
    );
};