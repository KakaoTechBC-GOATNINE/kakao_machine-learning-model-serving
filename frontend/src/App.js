import './App.css';
import Header from "./components/Header";
import {Routes, Route} from "react-router-dom";
import Recommend from "./pages/Recommend";
import Qna from "./pages/Qna";
import Login from "./pages/Login";
import CssBaseline from "@mui/material/CssBaseline";
import * as React from "react";
import NewQna from "./pages/NewQna";
import SignUp from "./pages/SignUp";

function App() {
    return (
        <div>
            <CssBaseline />
            <Header/>
            <Routes>
                <Route path="/"/>
                <Route path="/recommend" element={<Recommend/>}/>
                <Route path="/qnas" element={<Qna/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/qnas/new" element={<NewQna/>}/>
                <Route path="/sign-up" element={<SignUp/>}/>
            </Routes>
        </div>
    );
}

export default App;
