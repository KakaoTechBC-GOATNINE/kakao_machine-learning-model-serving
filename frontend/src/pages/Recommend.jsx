import * as React from 'react';
import Container from "@mui/material/Container";
import LocationFinder from "../components/LocationFinder";

export default function Recommend() {

    return (
        <Container component="main" maxWidth="sm">
            <LocationFinder />
        </Container>
        // <Grid container spacing={2}>
        //     <Grid item xs={4}>
        //         <FindLocation/>
        //     </Grid>
        //     <Grid item xs={8}>
        //         hi
        //     </Grid>
        // </Grid>
        // <Container component="main" maxWidth="xs">
        //     <Grid container spacing={2}>
        //         <Grid item xs={4}>
        //             <FindLocation />
        //         </Grid>
        //         <Grid item xs={8}>
        //             가게 리스트 노출 영역
        //         </Grid>
        //     </Grid>
        // </Container>
    );
}