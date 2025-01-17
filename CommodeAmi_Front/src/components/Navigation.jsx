import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux'; // Redux 사용
import { logout } from '../store/slices/authSlice'; // 로그아웃 액션 가져오기
import { searchMovies } from '../api/searchMovies';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Divider } from 'primereact/divider';
import LoginPage from '../views/user/LoginPage'; // LoginPage 컴포넌트 가져오기
import SignupPage from '../views/user/SignupPage';
import './Navigation.css';

function Navigation() {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    // Redux 상태에서 로그인 여부 확인
    const { isAuthenticated } = useSelector((state) => state.auth);

    const [value, setValue] = useState('');
    const [movies, setMovies] = useState([]);
    const [showResults, setShowResults] = useState(false);
    const [showDialog, setShowDialog] = useState(false); // Dialog 표시 상태
    const [showSignupDialog, setShowSignupDialog] = useState(false);
    const [formData, setFormData] = useState({ email: '', password: '' }); // 상태를 부모에서 관리
    const [signupFormData, setSignupFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        gender: '',
    });
    const [error, setError] = useState(''); // 에러 메시지 상태도 부모에서 관리


    const handleLogout = () => {
        dispatch(logout());
        setFormData({ email: '', password: '' }); // 폼 데이터 초기화
        setError(''); // 에러 메시지도 초기화
        navigate('/'); // 로그아웃 후 메인 페이지로 이동
    };
    // const handleSearch = async () => {
    //     if (!value.trim()) {
    //         alert('검색어를 입력하세요.');
    //         return;
    //     }
    
    //     try {
    //         console.log("Search started with value:", value); // 검색 시작 로깅
    //         const results = await searchMovies(value);
    //         console.log("Search results:", results); // 검색 결과 로깅
    //         setMovies(results || []); // 검색 결과 저장
    //         setShowResults(true); // 검색 결과 표시
    //     } catch (error) {
    //         console.error("Search failed:", error.response || error.message || error);
    //         alert('영화 검색 중 오류가 발생했습니다.');
    //         setMovies([]);
    //     }
    // };

    const handleSearch = () => {
        if (!value.trim()) {
            alert('검색어를 입력하세요.');
            return;
        }
    
        // 검색어 기반으로 URL 변경
        navigate(`/search?query=${encodeURIComponent(value)}`);
    };

    
    return (
        <>
            <nav className="navigation-bar">
                <div className="logo">
                    <Link to={isAuthenticated ? "/dashboard" : "/"}>
                        <h1>commode ami</h1>
                    </Link>
                </div>
                {/* <div className="search-bar">
                    <InputText
                        value={value}
                        placeholder="검색어를 입력하세요"
                        onChange={(e) => setValue(e.target.value)}
                    />
                </div> */}
                {/* 검색 바 */}
                <div className="search-bar">
                    <InputText
                        value={value}
                        placeholder="영화를 검색하세요"
                        onChange={(e) => setValue(e.target.value)}
                        onKeyDown={(e) => {
                            if (e.key === 'Enter') {
                                handleSearch(); // 엔터 키를 누르면 검색 실행
                            }
                        }}
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
                            <div className="link-button">
                                <Button 
                                    label="회원가입" 
                                    onClick={()=> setShowSignupDialog(true)}
                                />
                            </div>
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

            {/* SignupPage 컴포넌트 */}
            <SignupPage
                showDialog={showSignupDialog}
                setShowDialog={setShowSignupDialog}
                formData={signupFormData}
                setFormData={setSignupFormData}
            />
        </>
    );
}

export default Navigation;
