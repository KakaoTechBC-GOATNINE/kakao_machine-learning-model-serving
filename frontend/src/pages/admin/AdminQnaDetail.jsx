import * as React from 'react';
import { useParams } from 'react-router-dom';
import Container from "@mui/material/Container";
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Grid from "@mui/material/Unstable_Grid2";
import Button from "@mui/material/Button";

const data = {
    id: 1,
    category: '일반 질문',
    title: '일반 비공개 질문제목이에용',
    user: '박상은',
    isBlind: true,
    isAnswer: false,
    createdDate: '2024-08-02',
    content: '이것은 질문의 상세 내용입니다. 여기에 질문의 구체적인 내용이 들어갑니다.',
    answer: '이것은 답변의 내용입니다. 답변 내용이 여기에 들어갑니다.'
};

// TODO: 답변을 작성하지 않은 경우에만 [답변 달기] 버튼 출력
export default function QnaDetail() {
    const { id } = useParams();

    return (
        <Container component="main" maxWidth="md" sx={{ marginTop: 4, marginBottom: 4 }}>
            <Box sx={{ marginBottom: 2 }}>
                <Typography variant="h5" align="center" gutterBottom>
                    {data.title}
                </Typography>
                <Box display="flex" justifyContent="space-between" alignItems="center">
                    <Typography variant="body1">
                        <strong>ID:</strong> {data.id}
                    </Typography>
                    <Typography variant="body1">
                        <strong>유형:</strong> {data.category}
                    </Typography>
                    <Typography variant="body1">
                        <strong>작성자:</strong> {data.user}
                    </Typography>
                </Box>
                <Box display="flex" justifyContent="space-between" alignItems="center" mt={1}>
                    <Typography variant="body1">
                        <strong>답변 상태:</strong> <span style={{ color: data.isAnswer ? 'green' : 'red' }}>
                            {data.isAnswer ? '답변 완료' : '답변 대기'}
                        </span>
                    </Typography>
                    <Typography variant="body1">
                        <strong>작성일자:</strong> {data.createdDate}
                    </Typography>
                </Box>
            </Box>

            <Box sx={{ marginTop: 4 }}>
                <Typography variant="h6" gutterBottom>
                    내용
                </Typography>
                <Typography variant="body1" paragraph>
                    {data.content}
                </Typography>

                <Typography variant="h6" gutterBottom>
                    답변
                </Typography>
                <Typography variant="body1">
                    {data.answer}
                </Typography>
            </Box>
            <Grid container spacing={2} sx={{marginTop: '30px'}}>
                <Grid xs={4}></Grid>
                <Grid xs={4}>
                    <Button sx={{ml: 1}} variant="contained" size="small" fullWidth>답변 달기</Button>
                </Grid>
            </Grid>
        </Container>
    );
}