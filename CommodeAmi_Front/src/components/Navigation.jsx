import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Divider } from 'primereact/divider';
import './Navigation.css';

function Navigation({ isLoggedIn }) {
    const navigate = useNavigate();
    const [value, setValue] = useState('');

    return (
        <>
        <nav className="navigation-bar">
            {/* <div > */}
            <div className="logo">
                <Link to={isLoggedIn ? "/dashboard": "/"}>
                    <h1>commode ami</h1>
                </Link>
            </div>
            <div className="search-bar">
                <InputText value={value} placeholder="검색어를 입력하세요" onChange={(e) => setValue(e.target.value)} />
            </div>

            <div className='auth-buttons'>
                {/* 로그인이 안 되어있으면 로그인&회원 가입 버튼을, 로그인 되어있으면 마이페이지&로그아웃 버튼  */}
                {!isLoggedIn ? (
                <>
                    <Link to="/login" className="link-button">
                        <Button label="로그인" />
                    </Link>
                    <Link to="/signup" className="link-button">
                        <Button label="회원가입" />
                    </Link>
                </>
                ) : (
                <>
                    <Link to="/mypage" className="link-button">
                        <Button label="마이페이지" />
                    </Link>
                    <div className="logout-button">
                        <Button label="로그아웃"  />
                    </div>
                </>
                )}
            </div>
            {/* </div> */}

        </nav>
        <div className='divide-line'>
            <Divider />
        </div>
        </>
    );
}
export default Navigation;