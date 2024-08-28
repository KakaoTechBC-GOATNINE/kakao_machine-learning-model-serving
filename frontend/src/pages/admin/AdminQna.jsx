import Container from "@mui/material/Container";
import AdminQnaList from "../../components/admin/AdminQnaList";
import PageHeader from "../PageHeader";

export default function Qna() {
    return (
        <Container component="main" maxWidth="lg" sx={{ marginTop: 4 }}>
            <PageHeader text={"Q&A 관리"}></PageHeader>
            <AdminQnaList/>
        </Container>
    );
};