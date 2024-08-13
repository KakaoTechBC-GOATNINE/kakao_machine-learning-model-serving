import Container from "@mui/material/Container";
import Button from "@mui/material/Button";
import {Grid} from "@mui/material";
import {useNavigate} from "react-router-dom";

const Qna = () => {
    const nav = useNavigate();

    const onClickNewQna = () => {
        nav('/qnas/new');
    };
    return (
        <Container component="main" maxWidth="lg" sx={{ marginTop: 4 }}>
            <Grid container spacing={2}>
                <Grid item xs={10}></Grid>
                <Grid item xs={2}>
                    <Button fullWidth variant="contained" onClick={onClickNewQna}>문의 작성하기</Button>
                </Grid>
                <Grid item xs={12}>
                </Grid>
            </Grid>
        </Container>
    );
};

export default Qna;