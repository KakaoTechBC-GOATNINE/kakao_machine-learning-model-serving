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

const columns = [
    { id: 'id', label: 'id', minWidth: 10, align: 'center' },
    { id: 'category', label: '유형', minWidth: 30, align: 'center' },
    { id: 'title', label: '제목', minWidth: 100, align: 'center' },
    { id: 'user', label: '작성자', minWidth: 100, align: 'center' },
    { id: 'isAnswer', label: '답변 상태', minWidth: 30, align: 'center' },
    { id: 'createdDate', label: '작성일자', minWidth: 30, align: 'center' },
];

function createData(id, category, title, user, isBlind, isAnswer, createdDate) {
    return {id, category, title, user, isBlind, isAnswer, createdDate};
}

const rows = [
    createData(1, '일반 질문', '일반 비공개 질문제목이에용', '박상은', true, false, '2024-08-02'),
    createData(2, '일반 질문', '일반 공개 질문제목이에용', '박상은', false, false, '2024-08-02'),
    createData(3, '일반 질문', '일반 공개 & 답변 완료 질문제목이에용', '박상은', false, true, '2024-08-02'),
];

export default function QnaList() {
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    return (
        <Paper sx={{ width: '100%', overflow: 'hidden' }}>
            <TableContainer sx={{ maxHeight: 440 }}>
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
                            .map((row) => {
                                return (
                                    <TableRow hover role="checkbox" tabIndex={-1} key={row.category}>
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
                                );
                            })}
                    </TableBody>
                </Table>
            </TableContainer>
            <TablePagination
                rowsPerPageOptions={[10, 25, 100]}
                component="div"
                count={rows.length}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />
        </Paper>
    );
}