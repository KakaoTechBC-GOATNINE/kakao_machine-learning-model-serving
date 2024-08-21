import * as React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Container from "@mui/material/Container";
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Grid from "@mui/material/Unstable_Grid2";
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';
import axios from 'axios';
import Alert from '@mui/material/Alert';

export default function QnaDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [loading, setLoading] = React.useState(true);
    const [qnaData, setQnaData] = React.useState(null);
    const [error, setError] = React.useState(null);
    const [imageUrls, setImageUrls] = React.useState([]);

    React.useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            const token = getCookie("accessToken");

            try {
                const response = await axios.get(`http://localhost:8080/api/v1/qnas/${id}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                const data = response.data;
                setQnaData(data);

                const imagePromises = data.images.map(async (image) => {
                    const imageResponse = await axios.get(`http://localhost:8080/api/v1/qnas/image/${image.uuidName}`, {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                        responseType: 'blob',
                    });
                    return {
                        url: URL.createObjectURL(imageResponse.data),
                        originName: image.originName
                    };
                });

                const urls = await Promise.all(imagePromises);
                setImageUrls(urls);
            } catch (error) {
                if (error.response && error.response.status === 400) {
                    alert('권한이 없습니다.');
                    navigate('/qnas');
                } else {
                    setError('Q&A 데이터를 불러오는데 실패했습니다.');
                    console.error('Failed to fetch Q&A data:', error);
                }
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [id, navigate]);

    const handleEdit = () => {
        navigate(`/qnas/edit/${id}`);
    };

    const handleDelete = async () => {
        if (window.confirm("정말 삭제하시겠습니까?")) {
            try {
                const token = getCookie("accessToken");
                await axios.delete(`http://localhost:8080/api/v1/qnas/${id}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                alert("삭제되었습니다.");
                navigate('/qnas');
            } catch (error) {
                console.error("삭제 실패:", error);
                alert("삭제에 실패했습니다.");
            }
        }
    };

    function formatDateTime(dateArray) {
        if (!dateArray || dateArray.length < 6) {
            return "Invalid Date";
        }

        const year = String(dateArray[0]);
        const month = String(dateArray[1]).padStart(2, '0');
        const day = String(dateArray[2]).padStart(2, '0');
        const hour = String(dateArray[3]).padStart(2, '0');
        const minute = String(dateArray[4]).padStart(2, '0');

        return `${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분`;
    }

    const handleImageError = (index) => {
        setImageUrls((prevUrls) =>
            prevUrls.map((img, i) =>
                i === index ? { ...img, url: null } : img
            )
        );
    };

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Container component="main" maxWidth="md" sx={{ marginTop: 4, marginBottom: 4 }}>
                <Alert severity="error">{error}</Alert>
            </Container>
        );
    }

    return (
        <Container component="main" maxWidth="md" sx={{ marginTop: 4, marginBottom: 4 }}>
            <Box sx={{ marginBottom: 2 }}>
                <Typography variant="h6" align="center" color="textSecondary" gutterBottom>
                    {qnaData.category}
                </Typography>
                <Box display="flex" justifyContent="space-between" alignItems="center">
                    <Typography variant="body1">
                        <strong>글 번호:</strong> {qnaData.id}
                    </Typography>
                    <Typography variant="body1">
                        <strong>작성자:</strong> {qnaData.user.nickname}
                    </Typography>
                </Box>
                <Box display="flex" justifyContent="space-between" alignItems="center" mt={1}>
                    <Typography variant="body1">
                        <strong>답변 상태:</strong> <span style={{ color: qnaData.isAnswer ? 'green' : 'red' }}>
                            {qnaData.isAnswer ? '답변 완료' : '답변 대기'}
                        </span>
                    </Typography>
                    <Typography variant="body1">
                        <strong>작성일자:</strong> {formatDateTime(qnaData.createdDate)}
                    </Typography>
                </Box>
            </Box>

            <Box sx={{ marginTop: 6, marginBottom: 2, borderBottom: '1px solid #ddd', paddingBottom: 2 }}>
                <Typography variant="h4" align="center" gutterBottom>
                    {qnaData.title}
                </Typography>
            </Box>

            <Box sx={{ marginTop: 4 }}>
                {/* 이미지 표시 */}
                <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginBottom: 4 }}>
                    {imageUrls.map((img, index) => (
                        img.url ? (
                            <img
                                key={index}
                                src={img.url}
                                alt={img.originName}
                                style={{ maxWidth: '100%', marginBottom: '16px' }}
                                onError={() => handleImageError(index)}
                            />
                        ) : (
                            <Typography key={index} variant="body1" color="textSecondary">
                                {img.originName}
                            </Typography>
                        )
                    ))}
                </Box>

                <Typography variant="body1" paragraph sx={{ whiteSpace: 'pre-wrap', backgroundColor: '#f9f9f9', padding: 2, borderRadius: 1 }}>
                    {qnaData.content}
                </Typography>

                {qnaData.isAnswer && (
                    <>
                        <Typography variant="h6" gutterBottom>
                            답변
                        </Typography>
                        <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap', backgroundColor: '#f1f8e9', padding: 2, borderRadius: 1 }}>
                            {qnaData.answer}
                        </Typography>
                    </>
                )}
            </Box>
            <Grid container spacing={2} sx={{ marginTop: '30px', justifyContent: 'flex-end' }}>
                <Grid xs={3}>
                    <Button variant="contained" size="large" fullWidth onClick={handleEdit}>
                        수정
                    </Button>
                </Grid>
                <Grid xs={3}>
                    <Button
                        variant="contained"
                        color="error"
                        size="large"
                        fullWidth
                        onClick={handleDelete}
                    >
                        삭제
                    </Button>
                </Grid>
            </Grid>
        </Container>
    );
}

function getCookie(name) {
    const matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([.$?*|{}()[]\\\/+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}
