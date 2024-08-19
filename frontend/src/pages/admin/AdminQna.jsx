import Container from "@mui/material/Container";
import Button from "@mui/material/Button";
import {Grid} from "@mui/material";
import {useNavigate} from "react-router-dom";
import QnaList from "../../components/user/QnaList";
import AdminQnaList from "../../components/admin/AdminQnaList";

const Qna = () => {
    const nav = useNavigate();

    const onClickNewQna = () => {
        nav('/qnas/new');
    };
    return (
        <Container component="main" maxWidth="lg" sx={{ marginTop: 4 }}>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <AdminQnaList/>
                </Grid>
            </Grid>
        </Container>
    );
};

export default Qna;