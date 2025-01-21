// import React, { useState } from 'react';
// import { Link, useNavigate } from 'react-router-dom';
// import { useDispatch, useSelector } from 'react-redux'; // Redux 사용
// import { logout } from '../store/slices/authSlice'; // 로그아웃 액션 가져오기
// import { searchMovies } from '../api/searchMovies';
// import { InputText } from 'primereact/inputtext';
// import { Button } from 'primereact/button';
// import { Divider } from 'primereact/divider';
// import LoginPage from '../views/user/LoginPage'; // LoginPage 컴포넌트 가져오기
// import SignupPage from '../views/user/SignupPage';
// import './Navigation.css';

// function Navigation() {
//     const navigate = useNavigate();
//     const dispatch = useDispatch();

//     // Redux 상태에서 로그인 여부 확인
//     const { isAuthenticated } = useSelector((state) => state.auth);

//     const [value, setValue] = useState('');
//     const [movies, setMovies] = useState([]);
//     const [showResults, setShowResults] = useState(false);
//     const [showDialog, setShowDialog] = useState(false); // Dialog 표시 상태
//     const [showSignupDialog, setShowSignupDialog] = useState(false);
//     const [formData, setFormData] = useState({ email: '', password: '' }); // 상태를 부모에서 관리
//     const [signupFormData, setSignupFormData] = useState({
//         username: '',
//         email: '',
//         password: '',
//         confirmPassword: '',
//         nickname: '',
//         gender: '',
//     });
//     const [error, setError] = useState(''); // 에러 메시지 상태도 부모에서 관리


//     const handleLogout = () => {
//         dispatch(logout());
//         setFormData({ email: '', password: '' }); // 폼 데이터 초기화
//         setError(''); // 에러 메시지도 초기화
//         navigate('/'); // 로그아웃 후 메인 페이지로 이동
//     };

//     const handleSearch = () => {
//         if (!value.trim()) {
//             alert('검색어를 입력하세요.');
//             return;
//         }
    
//         // 검색어 기반으로 URL 변경
//         navigate(`/search?query=${encodeURIComponent(value)}`);
//     };

    
//     return (
//         <>
//             <nav className="navigation-bar">
//                 <div className="logo">
//                     <Link to={isAuthenticated ? "/dashboard" : "/"}>
//                         <h1>commode ami</h1>
//                     </Link>
//                 </div>
//                 <div className="search-bar">
//                     <InputText
//                         value={value}
//                         placeholder="영화를 검색하세요"
//                         onChange={(e) => setValue(e.target.value)}
//                         onKeyDown={(e) => {
//                             if (e.key === 'Enter') {
//                                 handleSearch(); // 엔터 키를 누르면 검색 실행
//                             }
//                         }}
//                     />
//                 </div>

//                 <div className="auth-buttons">
//                     {!isAuthenticated ? (
//                         <>
//                             <div className="link-button">
//                                 <Button
//                                     label="로그인"
//                                     onClick={() => setShowDialog(true)} // 로그인 버튼 클릭 시 Dialog 열기
                                
//                                 />
//                             </div>
//                             <div className="link-button">
//                                 <Button 
//                                     label="회원가입" 
//                                     onClick={()=> setShowSignupDialog(true)}
//                                 />
//                             </div>
//                         </>
//                     ) : (
//                         <>
//                             <Link to="/mypage" className="link-button">
//                                 <Button label="마이페이지" />
//                             </Link>
//                             <div className="logout-button">
//                                 <Button label="로그아웃" onClick={handleLogout} />
//                             </div>
//                             <div className="config-button">
//                                 <i className="pi pi-cog" ></i>
//                             </div>
//                         </>
//                     )}
//                 </div>
//             </nav>
//             <div className="divide-line">
//                 <Divider />
//             </div>

//             <LoginPage
//                 showDialog={showDialog}
//                 setShowDialog={setShowDialog}
//                 formData={formData}
//                 setFormData={setFormData}
//                 error={error}
//                 setError={setError}
//             />

//             {/* SignupPage 컴포넌트 */}
//             <SignupPage
//                 showDialog={showSignupDialog}
//                 setShowDialog={setShowSignupDialog}
//                 formData={signupFormData}
//                 setFormData={setSignupFormData}
//             />
//         </>
//     );
// }

// export default Navigation;

import React, { useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { logout } from '../store/slices/authSlice';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Divider } from 'primereact/divider';
import { Menu } from 'primereact/menu';
import { Dialog } from 'primereact/dialog';
import LoginPage from '../views/user/LoginPage';
import SignupPage from '../views/user/SignupPage';
import axios from 'axios';
import './Navigation.css';

function Navigation() {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const { isAuthenticated, user } = useSelector((state) => state.auth);

    const [value, setValue] = useState('');
    const [showDialog, setShowDialog] = useState(false); // 로그인 Dialog
    const [showSignupDialog, setShowSignupDialog] = useState(false); // 회원가입 Dialog
    const [showPasswordDialog, setShowPasswordDialog] = useState(false); // 비밀번호 변경 Dialog
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [formData, setFormData] = useState({ email: '', password: '' }); // 상태를 부모에서 관리
    const [signupFormData, setSignupFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        gender: '',
    });
    const menu = useRef(null);
    const userId = user?.userId;
    const handleLogout = () => {
        dispatch(logout());
        navigate('/');
    };

// handlePasswordChange 함수 수정
const handlePasswordChange = async () => {
    if (!currentPassword || !newPassword || !confirmPassword) {
        setError('모든 필드를 입력해주세요.');
        return;
    }

    if (newPassword !== confirmPassword) {
        setError('새 비밀번호와 확인 비밀번호가 일치하지 않습니다.');
        return;
    }

    try {
        // 비밀번호 변경 API 호출
        const response = await axios.put(`/api/user/password/${userId}`, {
            currentPwd: currentPassword,
            newPwd: newPassword,
        });

        console.log(response.data);

        // 성공 처리
        setCurrentPassword('');
        setNewPassword('');
        setConfirmPassword('');
        setError('');
        setShowPasswordDialog(false);
        alert('비밀번호가 성공적으로 변경되었습니다.');
    } catch (error) {
        console.error(error);
        setError(error.response?.data?.message || '비밀번호 변경에 실패했습니다.');
    }
};

    const handleSearch = () => {
        if (!value.trim()) {
            alert('검색어를 입력하세요.');
            return;
        }
        navigate(`/search?query=${encodeURIComponent(value)}`);
    };

    const menuItems = [
        {
            label: '마이페이지',
            icon: 'pi pi-user',
            command: () => navigate('/mypage'),
        },
        {
            label: '비밀번호 변경',
            icon: 'pi pi-key',
            command: () => setShowPasswordDialog(true),
        },
        {
            label: '로그아웃',
            icon: 'pi pi-sign-out',
            command: () => handleLogout(),
        },
    ];

    return (
        <>
            <nav className="navigation-bar">
                <div className="logo">
                    <Link to={isAuthenticated ? '/dashboard' : '/'}>
                        <h1>commode ami</h1>
                    </Link>
                </div>
                <div className="search-bar">
                    <InputText
                        value={value}
                        placeholder="영화를 검색하세요"
                        onChange={(e) => setValue(e.target.value)}
                        onKeyDown={(e) => {
                            if (e.key === 'Enter') {
                                handleSearch();
                            }
                        }}
                    />
                </div>
                <div className="auth-buttons">
                    {!isAuthenticated ? (
                        <>
                            <Button label="로그인" onClick={() => setShowDialog(true)} />
                            <Button label="회원가입" onClick={() => setShowSignupDialog(true)} />
                        </>
                    ) : (
                        <>
                            <i
                                className="pi pi-cog"
                                style={{ fontSize: '1.5rem', cursor: 'pointer' }}
                                onClick={(event) => menu.current.toggle(event)}
                            ></i>
                            <Menu model={menuItems} popup ref={menu} />
                        </>
                    )}
                </div>
            </nav>
            <Divider />

            {/* 로그인 Dialog */}
            <LoginPage
                showDialog={showDialog}
                setShowDialog={setShowDialog}
                formData={formData}
                setFormData={setFormData}
                error={error}
                setError={setError}
            />

            {/* 회원가입 Dialog */}
            <SignupPage
                showDialog={showSignupDialog}
                setShowDialog={setShowSignupDialog}
                formData={signupFormData}
                setFormData={setSignupFormData}
                // setFormData={() => {}}
            />

            {/* 비밀번호 변경 Dialog */}
            <Dialog
                header="비밀번호 변경"
                visible={showPasswordDialog}
                style={{ width: '30vw' }}
                onHide={() => setShowPasswordDialog(false)}
            >
                <div className="password-change-dialog">
                    <div className="form-group">
                        <label htmlFor="current-password">현재 비밀번호</label>
                        <InputText
                            id="current-password"
                            type="password"
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="new-password">새 비밀번호</label>
                        <InputText
                            id="new-password"
                            type="password"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="confirm-password">새 비밀번호 확인</label>
                        <InputText
                            id="confirm-password"
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                        />
                    </div>
                    {error && <p className="error-message">{error}</p>}
                    <Button label="비밀번호 변경" onClick={handlePasswordChange} />
                </div>
            </Dialog>
        </>
    );
}

export default Navigation;
