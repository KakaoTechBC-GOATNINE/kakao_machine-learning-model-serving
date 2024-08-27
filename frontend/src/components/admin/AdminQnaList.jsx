import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import LockIcon from '@mui/icons-material/Lock';
import CircularProgress from '@mui/material/CircularProgress';
import { useNavigate } from 'react-router-dom';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import Select from '@mui/material/Select';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Box from '@mui/material/Box';
import Pagination from '@mui/material/Pagination';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import api from '../Api';

const columns = [
    { id: 'id', label: '글 번호', minWidth: 10, align: 'center' },
    { id: 'category', label: '유형', minWidth: 30, align: 'center' },
    { id: 'title', label: '제목', minWidth: 100, align: 'center' },
    { id: 'user', label: '작성자', minWidth: 100, align: 'center' },
    { id: 'isAnswer', label: '답변 상태', minWidth: 30, align: 'center' },
    { id: 'createdDate', label: '작성일자', minWidth: 30, align: 'center' },
];

// 카테고리 맵핑
const categoryMap = {
    "전체": "",
    "일반 질문": "GENERAL",
    "계정 관련": "ACCOUNT",
    "기술 지원": "TECH_SUPPORT",
    "기타": "OTHER",
};

// 날짜 배열을 원하는 형식으로 변환하는 함수
function formatDateTime(dateArray) {
    if (!dateArray || dateArray.length < 6) {
        return "Invalid Date";
    }

    const year = String(dateArray[0]).slice(2); // '2024' -> '24'
    const month = String(dateArray[1]).padStart(2, '0'); // '8' -> '08'
    const day = String(dateArray[2]).padStart(2, '0'); // '20'
    const hour = String(dateArray[3]).padStart(2, '0'); // '16'
    const minute = String(dateArray[4]).padStart(2, '0'); // '16'

    // 원하는 형식으로 변환
    return `${year}.${month}.${day} ${hour}:${minute}`;
}

export default function AdminQnaList() {
    const [page, setPage] = React.useState(1);
    const [rowsPerPage, setRowsPerPage] = React.useState(10); // 클라이언트 측에서만 관리
    const [rows, setRows] = React.useState([]);
    const [loading, setLoading] = React.useState(true);
    const [totalRows, setTotalRows] = React.useState(0);
    const [title, setTitle] = React.useState('');
    const [category, setCategory] = React.useState('');
    const [notAnswered, setNotAnswered] = React.useState(false);  // "미답변 질문만 보기" 상태 추가
    const navigate = useNavigate();

    React.useEffect(() => {
        fetchData();  // 페이지, "미답변 질문만 보기" 값이 변경될 때마다 데이터 로드
    }, [page, notAnswered]);

    const fetchData = async () => {
        setLoading(true);

        try {
            const response = await api.get('/api/v1/admins/qnas', {
                params: {
                    page: page - 1,  // 서버에서 0 기반 페이지 처리
                    title: title,
                    category: categoryMap[category],   // 카테고리 맵핑된 값 사용
                    notAnswered: notAnswered,  // "미답변 질문만 보기" 값 추가
                },
            });

            const data = response.data;
            setRows(data.content.map((item) => ({
                id: item.id,
                category: item.category,
                title: item.title,
                user: item.user.nickname,
                isBlind: item.isBlind,
                isAnswer: item.isAnswer,
                createdDate: formatDateTime(item.createdDate),  // 날짜 형식 변환
            })));
            setTotalRows(data.totalElements);
        } catch (error) {
            if (error.response && (error.response.status === 400 ||
                error.response.status === 401 ||
                error.response.status === 403)) {
                alert('관리자만 접근 가능합니다.');
                navigate('/');
            } else {
                console.error('Failed to fetch data', error);
            }
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = () => {
        setPage(1);  // 페이지를 초기화하고
        fetchData();  // 검색을 수행
    };

    const handleChangePage = (event, value) => {
        setPage(value);
    };

    const handleRowClick = (id) => {
        navigate(`/admin/qnas/${id}`);
    };

    const handleCategoryChange = (event) => {
        setCategory(event.target.value);
    };

    const handleTitleChange = (event) => {
        setTitle(event.target.value);
    };

    const handleNotAnsweredChange = (event) => {
        setNotAnswered(event.target.checked);
    };

    // 엔터키 입력 시 검색 함수
    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            handleSearch();  // 엔터키 입력 시 검색 수행
        }
    };

    return (
        <Paper sx={{ width: '100%', overflow: 'hidden' }}>
            {/* 검색 바 */}
            <Box sx={{ padding: '16px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <FormControl variant="outlined" size="small" style={{ minWidth: 150 }}>
                    <InputLabel>카테고리</InputLabel>
                    <Select
                        value={category}
                        onChange={handleCategoryChange}
                        label="카테고리"
                    >
                        <MenuItem value="">
                            <em>전체</em>
                        </MenuItem>
                        <MenuItem value="일반 질문">일반 질문</MenuItem>
                        <MenuItem value="계정 관련">계정 관련</MenuItem>
                        <MenuItem value="기술 지원">기술 지원</MenuItem>
                        <MenuItem value="기타">기타</MenuItem>
                    </Select>
                </FormControl>
                <TextField
                    label="제목"
                    variant="outlined"
                    value={title}
                    onChange={handleTitleChange}
                    onKeyDown={handleKeyDown}  // 엔터키 입력 시 검색
                    size="small"
                />
                <Button variant="contained" color="primary" onClick={handleSearch}>
                    검색
                </Button>
                <FormControlLabel
                    control={
                        <Checkbox
                            checked={notAnswered}
                            onChange={handleNotAnsweredChange}
                            color="primary"
                        />
                    }
                    label={
                        <span style={{ color: notAnswered ? 'red' : 'inherit' }}>
                            답변 대기만 보기
                        </span>
                    }
                    sx={{ marginLeft: 'auto' }}  // 체크박스를 오른쪽으로 이동
                />
            </Box>
            {/* 테이블 */}
            <TableContainer>
                {loading ? (
                    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
                        <CircularProgress />
                    </div>
                ) : (
                    <Table stickyHeader aria-label="sticky table">
                        <TableHead>
                            <TableRow>
                                {columns.map((column) => (
                                    <TableCell
                                        key={column.id}
                                        align={column.align}
                                        style={{ minWidth: column.minWidth }}
                                    >
                                        {column.label}
                                    </TableCell>
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <TableRow
                                    hover
                                    role="checkbox"
                                    tabIndex={-1}
                                    key={row.id}
                                    onClick={() => handleRowClick(row.id)}
                                    style={{ cursor: 'pointer' }}
                                >
                                    {columns.map((column) => {
                                        const value = row[column.id];
                                        if (column.id === 'isAnswer') {
                                            return (
                                                <TableCell key={column.id} align={column.align}>
                                                    <span style={{ color: value ? 'green' : 'red' }}>
                                                        {value ? '답변 완료' : '답변 대기'}
                                                    </span>
                                                </TableCell>
                                            );
                                        }
                                        return (
                                            <TableCell key={column.id} align={column.align}>
                                                {column.id === 'title' && row.isBlind && (
                                                    <LockIcon
                                                        style={{
                                                            marginRight: 4,
                                                            fontSize: '1em',
                                                            verticalAlign: 'middle'
                                                        }}
                                                    />
                                                )}
                                                {value}
                                            </TableCell>
                                        );
                                    })}
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                )}
            </TableContainer>
            {/* 페이징 */}
            <Box sx={{ display: 'flex', justifyContent: 'center', padding: '16px' }}>
                <Pagination
                    count={Math.ceil(totalRows / rowsPerPage)}
                    page={page}
                    onChange={handleChangePage}
                    color="primary"
                    showFirstButton
                    showLastButton
                />
            </Box>
        </Paper>
    );
}
