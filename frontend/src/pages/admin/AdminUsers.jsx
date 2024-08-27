import React, { useState, useEffect } from 'react';
import {
    Container,
    Box,
    Typography,
    TextField,
    Button,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    CircularProgress,
    Alert,
    InputAdornment,
    Pagination,
    IconButton,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import StarIcon from '@mui/icons-material/Star';
import ClearIcon from '@mui/icons-material/Clear';
import SortIcon from '@mui/icons-material/Sort';
import api from '../../components/Api';

export default function AdminUsers() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [searchName, setSearchName] = useState('');
    const [page, setPage] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [totalElements, setTotalElements] = useState(0);
    const [sortField, setSortField] = useState('');
    const [sortDirection, setSortDirection] = useState('');

    useEffect(() => {
        fetchUsers();
    }, [page, rowsPerPage, sortField, sortDirection]);

    const fetchUsers = async () => {
        setLoading(true);
        try {
            const sortParam = sortField ? `${sortField},${sortDirection}` : '';
            const response = await api.get('/api/v1/admins/users', {
                params: {
                    name: searchName,
                    page: page - 1,
                    size: rowsPerPage,
                    sort: sortParam,
                },
            });
            setUsers(response.data.content);
            setTotalElements(response.data.totalElements);
            setError(null);
        } catch (error) {
            console.error('Failed to fetch users:', error);
            setError('유저 데이터를 불러오는데 실패했습니다.');
        } finally {
            setLoading(false);
        }
    };

    const handleSearchChange = (event) => {
        setSearchName(event.target.value);
    };

    const handleSearch = () => {
        setPage(1);
        fetchUsers();
    };

    const handlePageChange = (event, value) => {
        setPage(value);
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    const handleSort = (field) => {
        if (sortField === field) {
            setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
        } else {
            setSortField(field);
            setSortDirection('asc');
        }
    };

    const handleRemoveSort = () => {
        setSortField('');
        setSortDirection('');
    };

    const formatCreatedDate = (dateString) => {
        const date = new Date(dateString);
        const year = String(date.getFullYear()).slice(-2);
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}.${month}.${day}`;
    };

    const formatProvider = (provider) => {
        switch (provider) {
            case 'KAKAO':
                return '카카오';
            case 'DEFAULT':
            default:
                return '일반';
        }
    };

    const formatRole = (role) => {
        return role === 'ADMIN' ? (
            <>
                <StarIcon sx={{ fontSize: 'small', color: 'gold', verticalAlign: 'middle' }} /> 관리자
            </>
        ) : (
            '사용자'
        );
    };

    const formatLoginStatus = (isLogin) => {
        return isLogin ? (
            <span style={{ color: 'green' }}>접속중</span>
        ) : (
            <span style={{ color: 'red' }}>미접속</span>
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
        <Container component="main" maxWidth="lg" sx={{ marginTop: 4, marginBottom: 4 }}>
            <Typography variant="h4" gutterBottom>
                유저 관리
            </Typography>

            <Box sx={{ display: 'flex', justifyContent: 'flex-start', alignItems: 'center', marginBottom: 2 }}>
                <TextField
                    label="닉네임 검색"
                    variant="outlined"
                    value={searchName}
                    onChange={handleSearchChange}
                    onKeyPress={handleKeyPress}
                    InputProps={{
                        startAdornment: (
                            <InputAdornment position="start">
                                <SearchIcon />
                            </InputAdornment>
                        ),
                    }}
                    sx={{ width: '300px', marginRight: 2 }}
                />
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleSearch}
                    sx={{ height: '100%' }}
                >
                    검색
                </Button>
            </Box>

            <TableContainer component={Paper} sx={{ borderRadius: 2, boxShadow: 3 }}>
                <Table>
                    <TableHead>
                        <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                            {['id', 'serialId', 'provider', 'role', 'createdDate', 'nickname', 'isLogin'].map((field) => (
                                <TableCell
                                    key={field}
                                    align="center"
                                    onClick={() => handleSort(field)}
                                    style={{ cursor: 'pointer', position: 'relative' }}
                                    sx={{
                                        '&:hover': {
                                            backgroundColor: '#e0e0e0',
                                        },
                                    }}
                                >
                                    {field === 'id' && 'ID'}
                                    {field === 'serialId' && '로그인 ID'}
                                    {field === 'provider' && '로그인 방법'}
                                    {field === 'role' && '등급'}
                                    {field === 'createdDate' && '생성 날짜'}
                                    {field === 'nickname' && '닉네임'}
                                    {field === 'isLogin' && '접속 여부'}
                                    {sortField === field && (
                                        <IconButton
                                            size="small"
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                handleRemoveSort();
                                            }}
                                            sx={{ position: 'absolute', right: 0, top: '50%', transform: 'translateY(-50%)', color: '#FF5722' }}
                                        >
                                            <ClearIcon fontSize="small" />
                                        </IconButton>
                                    )}
                                    <SortIcon fontSize="small" sx={{ verticalAlign: 'middle', marginLeft: 1 }} />
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {users.map((user) => (
                            <TableRow key={user.id} hover>
                                <TableCell align="center">{user.id}</TableCell>
                                <TableCell align="center">{user.serialId}</TableCell>
                                <TableCell align="center">{formatProvider(user.provider)}</TableCell>
                                <TableCell align="center">{formatRole(user.role)}</TableCell>
                                <TableCell align="center">{formatCreatedDate(user.createdDate)}</TableCell>
                                <TableCell align="center">{user.nickname}</TableCell>
                                <TableCell align="center">{formatLoginStatus(user.isLogin)}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Box sx={{ display: 'flex', justifyContent: 'center', padding: '16px' }}>
                <Pagination
                    count={Math.ceil(totalElements / rowsPerPage)}
                    page={page}
                    onChange={handlePageChange}
                    color="primary"
                    showFirstButton
                    showLastButton
                    siblingCount={1}
                    boundaryCount={1}
                />
            </Box>
        </Container>
    );
}
