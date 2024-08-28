import './App.css';
import Header from "./components/user/Header";
import AdminHeader from "./components/admin/AdminHeader";
import {Route, Routes, useLocation} from "react-router-dom";
import Recommend from "./pages/user/Recommend";
import Qna from "./pages/user/Qna";
import Login from "./pages/user/Login";
import CssBaseline from "@mui/material/CssBaseline";
import React from "react";
import NewQna from "./pages/user/NewQna";
import SignUp from "./pages/user/SignUp";
import SignUpKakao from "./pages/user/SignUpKakao";
import QnaDetail from "./pages/user/QnaDetail";
import EditQna from "./pages/user/EditQna";
import AdminQnaDetail from "./pages/admin/AdminQnaDetail";
import MyPage from "./pages/user/MyPage";
import AdminUsers from "./pages/admin/AdminUsers";
import AdminQna from "./pages/admin/AdminQna";
import RestaurantMap from "./pages/user/RestaurantMap"; 

function App() {
    const location = useLocation();
    const isAdminRoute = location.pathname.startsWith('/admin');

    return (
        <div>
            <CssBaseline />
            {isAdminRoute ? <AdminHeader /> : <Header />}
            <Routes>
                {/* 일반 사용자 경로 */}
                <Route path="/" element={<Recommend/>}/>
                <Route path="/recommend" element={<Recommend/>}/>
                <Route path="/qnas" element={<Qna/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/mypage" element={<MyPage/>}/>
                <Route path="/qnas/new" element={<NewQna/>}/>
                <Route path="/sign-up" element={<SignUp/>}/>
                <Route path="/sign-up-kakao" element={<SignUpKakao/>}/>
                <Route path="/qnas/:id" element={<QnaDetail/>}/>
                <Route path="/qnas/edit/:id" element={<EditQna/>}/>
                <Route path="/RestaurantMap" element={<RestaurantMap/>}/>

                {/* 관리자 경로 */}
                <Route path="/admin" element={<AdminQna/>}/>
                <Route path="/admin/users" element={<AdminUsers/>}/>
                <Route path="/admin/qnas" element={<AdminQna/>}/>
                <Route path="/admin/qnas/:id" element={<AdminQnaDetail />}/>
                {/* 추가적인 관리자 페이지들... */}
            </Routes>
        </div>
    );
}

export default App;