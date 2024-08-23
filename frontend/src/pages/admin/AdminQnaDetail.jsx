import * as React from 'react';
import { useParams } from 'react-router-dom';
import Container from "@mui/material/Container";
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Grid from "@mui/material/Unstable_Grid2";
import CustomButton from "../../components/CustomButton";
import TextField from '@mui/material/TextField';

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

export default function QnaDetail() {
    const { id } = useParams();
    const [isAnswering, setIsAnswering] = React.useState(false); // 상태 관리

    const handleAnswerClick = () => {
        setIsAnswering(true); // 버튼 클릭 시 상태 변경
    };

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
            {!isAnswering && (
                <Grid container spacing={2} sx={{ marginTop: '30px' }}>
                    <Grid xs={4}></Grid>
                    <Grid xs={4}>
                        <CustomButton text={"답변 달기"} variant={"contained"} onClick={handleAnswerClick} />
                    </Grid>
                </Grid>
            )}
            {isAnswering && (
                <Grid container spacing={2} sx={{ marginTop: '30px' }}>
                    <Grid xs={10}>
                        <TextField
                            label="답변 입력"
                            multiline
                            rows={4}
                            fullWidth
                            variant="outlined"
                        />
                    </Grid>
                    <Grid xs={2}>
                        <CustomButton text={"답변 달기"} variant={"contained"} sx={{ height: '100%' }}/>
                    </Grid>
                </Grid>
            )}
        </Container>
    );
}