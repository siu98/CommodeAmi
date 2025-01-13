import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import './MyPage.css';
import axios from 'axios';

const MyPage = () => {
    const user = useSelector((state) => state.auth.user);
    
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
                    <h3>리뷰</h3>
                    <h3>티켓</h3>
                </div>
            </div>
        </div>
    )
}

export default MyPage;