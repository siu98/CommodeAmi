import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux'; // Redux 사용
import { logout } from '../store/slices/authSlice'; // 로그아웃 액션 가져오기
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Divider } from 'primereact/divider';
import LoginPage from '../views/user/LoginPage'; // LoginPage 컴포넌트 가져오기
import './Navigation.css';

function Navigation() {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    // Redux 상태에서 로그인 여부 확인
    const { isAuthenticated } = useSelector((state) => state.auth);

    const [value, setValue] = useState('');
    const [showDialog, setShowDialog] = useState(false); // Dialog 표시 상태
    const [formData, setFormData] = useState({ email: '', password: '' }); // 상태를 부모에서 관리
    const [error, setError] = useState(''); // 에러 메시지 상태도 부모에서 관리


    const handleLogout = () => {
        dispatch(logout());
        setFormData({ email: '', password: '' }); // 폼 데이터 초기화
        setError(''); // 에러 메시지도 초기화
        navigate('/'); // 로그아웃 후 메인 페이지로 이동
    };

    return (
        <>
            <nav className="navigation-bar">
                <div className="logo">
                    <Link to={isAuthenticated ? "/dashboard" : "/"}>
                        <h1>commode ami</h1>
                    </Link>
                </div>
                <div className="search-bar">
                    <InputText
                        value={value}
                        placeholder="검색어를 입력하세요"
                        onChange={(e) => setValue(e.target.value)}
                    />
                </div>

                <div className="auth-buttons">
                    {!isAuthenticated ? (
                        <>
                            <div className="link-button">
                                <Button
                                    label="로그인"
                                    onClick={() => setShowDialog(true)} // 로그인 버튼 클릭 시 Dialog 열기
                                
                                />
                            </div>
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
                                <Button label="로그아웃" onClick={handleLogout} />
                            </div>
                        </>
                    )}
                </div>
            </nav>
            <div className="divide-line">
                <Divider />
            </div>

            {/* LoginPage 컴포넌트에 showDialog 전달 */}
            {/* <LoginPage showDialog={showDialog} setShowDialog={setShowDialog} /> */}
            <LoginPage
                showDialog={showDialog}
                setShowDialog={setShowDialog}
                formData={formData}
                setFormData={setFormData}
                error={error}
                setError={setError}
            />
        </>
    );
}

export default Navigation;
