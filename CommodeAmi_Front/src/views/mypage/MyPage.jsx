import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import EvaluationPage from './EvaluationPage';
import CustomTicket from '../customticket/CustomTicket';
import './MyPage.css';


const MyPage = () => {
    const [movies, setMovies] = useState([]);
    const user = useSelector((state) => state.auth.user);
    const userId = user?.userId;
    console.log("user 찍기", user);
    console.log("userId 찍기", userId);
    return (
        <div className="mypage">
            <div className="profile-container">
                <div className="profile-header">
                    <div className="profile-info">
                        <span className="text-bg">{user.nickName}님의 마이페이지</span>
                    </div>
                </div>
                <div className="profile-content">
                    <h3>평가</h3>
                    <EvaluationPage movies={movies} />
                    <h3>리뷰</h3>
                    <h3>티켓</h3>
                    <div className="ticket-list">
                        <CustomTicket/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default MyPage;