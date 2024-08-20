import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import LockIcon from '@mui/icons-material/Lock';
import CircularProgress from '@mui/material/CircularProgress';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function getCookie(name) {
    const matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([.$?*|{}()[]\\\/+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

const columns = [
    { id: 'id', label: 'id', minWidth: 10, align: 'center' },
    { id: 'category', label: '유형', minWidth: 30, align: 'center' },
    { id: 'title', label: '제목', minWidth: 100, align: 'center' },
    { id: 'user', label: '작성자', minWidth: 100, align: 'center' },
    { id: 'isAnswer', label: '답변 상태', minWidth: 30, align: 'center' },
    { id: 'createdDate', label: '작성일자', minWidth: 30, align: 'center' },
];

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

export default function QnaList() {
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const [rows, setRows] = React.useState([]);
    const [loading, setLoading] = React.useState(true);
    const [totalRows, setTotalRows] = React.useState(0);
    const navigate = useNavigate();

    React.useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            const token = getCookie("accessToken");  // 쿠키에서 토큰을 가져옴

            try {
                // const response = await axios.get('http://localhost:8080/api/v1/qnas', {
                const response = await axios.get('https://shortood-dev.shop/api/v1/qnas', {
                    params: {
                        page: page,
                        size: rowsPerPage,
                    },
                    headers: {
                        Authorization: `Bearer ${token}`,  // 인증 헤더에 토큰 추가
                    },
                });

                const data = response.data;
                setRows(data.content.map((item) => ({
                    id: item.id,
                    category: item.category,
                    title: item.content,
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
                    alert('로그인한 유저만 사용 가능합니다.');
                    navigate('/login');
                } else {
                    console.error('Failed to fetch data', error);
                }
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [page, rowsPerPage, navigate]);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    const handleRowClick = (id) => {
        navigate(`/qnas/${id}`);
    };

    return (
        <Paper sx={{ width: '100%', overflow: 'hidden' }}>
            <TableContainer sx={{ maxHeight: 440 }}>
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
                            {rows
                                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                .map((row) => (
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
            <TablePagination
                rowsPerPageOptions={[10, 25, 100]}
                component="div"
                count={totalRows}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />
        </Paper>
    );
}
