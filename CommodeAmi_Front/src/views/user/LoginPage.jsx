// import React, { useState } from 'react';
// import { useDispatch, useSelector } from 'react-redux';
// import { login, logout } from '../../store/slices/authSlice.js';

// function LoginPage() {
//     const dispatch = useDispatch();
//     const { user } = useSelector((state) => state.auth); // Redux 상태에서 user 가져오기
//     const [formData, setFormData] = useState({ email: '', password: '' });
//     const [error, setError] = useState('');

//     const handleChange = (e) => {
//         setFormData({
//             ...formData,
//             [e.target.name]: e.target.value,
//         });
//     };

//     const handleLogin = async (e) => {
//         e.preventDefault(); // 폼 제출 기본 동작 방지
//         const success = await dispatch(login(formData.email, formData.password));
//         if (success) {
//             console.log('Login successful');
//         } else {
//             console.error('Login failed');
//             setError('Invalid email or password'); // 에러 메시지 설정
//         }
//     };

//     const handleLogout = () => {
//         dispatch(logout());
//         console.log('Logged out');
//     };

//     return (
//         <div>
//             {user ? (
//                 <div>
//                     <p>Welcome, {user.userName}!</p>
//                     {/* <button onClick={handleLogout}>Logout</button> */}
//                 </div>
//             ) : (
//                 <div id="login-form-container">
//                     <div id="sign-in-container">
//                         <h3>로그인</h3>
//                         <form onSubmit={handleLogin}>
//                             <label htmlFor="email">이메일</label>
//                             <input
//                                 type="email"
//                                 name="email"
//                                 id="email"
//                                 placeholder="이메일"
//                                 value={formData.email}
//                                 onChange={handleChange}
//                                 required
//                             />

//                             <label htmlFor="password">비밀번호</label>
//                             <input
//                                 type="password"
//                                 name="password"
//                                 id="password"
//                                 placeholder="비밀번호"
//                                 value={formData.password}
//                                 onChange={handleChange}
//                                 required
//                             />

//                             <div id="form-controls">
//                                 <button type="submit">로그인</button>
//                             </div>

//                             <p style={{ fontSize: '12px', textAlign: 'center' }}>
//                                 <a href="/find-username" style={{ marginRight: '10px', color: '#3D6094' }}>아이디 찾기</a>
//                                 <a href="/reset-password" style={{ color: '#3D6094' }}>비밀번호 찾기</a>
//                             </p>

//                             {error && <p style={{ color: 'red' }}>{error}</p>}
//                         </form>
//                     </div>
//                 </div>
//             )}
//         </div>
//     );
// }

// export default LoginPage;


// import React, { useState, useEffect } from 'react';
// import { useDispatch, useSelector } from 'react-redux';
// import { login } from '../../store/slices/authSlice'; // login 액션 가져오기
// import { Dialog } from 'primereact/dialog'; // Dialog 컴포넌트
// import { Button } from 'primereact/button';
// import './LoginPage.css';

// function LoginPage({ showDialog, setShowDialog }) {
//     const dispatch = useDispatch();
//     const { user } = useSelector((state) => state.auth);
//     const [formData, setFormData] = useState({
//         email: '',
//         password: '',
//     });
//     const [error, setError] = useState('');

//     // 로그아웃 이후 또는 Dialog 닫힐 때 입력값 초기화
//     useEffect(() => {
//         if (!user) {
//             setFormData({ email: '', password: '' });
//             setError(''); // 에러 메시지도 초기화
//         }
//     }, [user, showDialog]);

//     const handleChange = (e) => {
//         setFormData({
//             ...formData,
//             [e.target.name]: e.target.value,
//         });
//     };

//     const handleLogin = async (e) => {
//         e.preventDefault();
//         const success = await dispatch(login(formData.email, formData.password));
//         if (success) {
//             console.log('Login successful');
//             setShowDialog(false); // 로그인 성공 시 Dialog 닫기
//         } else {
//             console.error('Login failed');
//             setError('Invalid email or password'); // 에러 메시지 설정
//         }
//     };

//     const dialogFooter = (
//         <div className="form-controls">
//             <div className="button-container">
//                 {/* 로그인 버튼 */}
//                 <Button
//                     label="로그인"
//                     icon="pi pi-check"
//                     onClick={handleLogin}
//                     autoFocus
//                     className="login-button"
//                 />
//             </div>
//             <div className="link-container">
//                 {/* 아이디 찾기와 비밀번호 찾기 */}
//                 <p style={{ fontSize: '12px', textAlign: 'center', marginTop: '1rem' }}>
//                     <a href="/find-username" style={{ marginRight: '10px', color: '#3D6094' }}>아이디 찾기</a>
//                     <a href="/reset-password" style={{ color: '#3D6094' }}>비밀번호 찾기</a>
//                 </p>
//             </div>
//         </div>
//     );

//     return (
//         <Dialog
//     header="로그인"
//     visible={showDialog}
//     className="custom-dialog"
//     footer={dialogFooter}
//     modal
//     onHide={() => setShowDialog(false)} // Dialog 닫기
// >
//     <form onSubmit={handleLogin} className="login-form-container">
//         <div className="form-group">
//             <label htmlFor="email">이메일</label>
//             <input
//                 type="email"
//                 name="email"
//                 id="email"
//                 placeholder="이메일"
//                 value={formData.email}
//                 onChange={handleChange}
//                 required
//             />
//         </div>

//         <div className="form-group">
//             <label htmlFor="password">비밀번호</label>
//             <input
//                 type="password"
//                 name="password"
//                 id="password"
//                 placeholder="비밀번호"
//                 value={formData.password}
//                 onChange={handleChange}
//                 required
//             />
//         </div>

//         {error && <p className="error-message">{error}</p>}
//     </form>
// </Dialog>
//     );
// }

// export default LoginPage;


import React, { useEffect, useRef } from 'react';
import { useDispatch } from 'react-redux';
import { login } from '../../store/slices/authSlice';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { Toast } from 'primereact/toast';
import './LoginPage.css';

function LoginPage({
    showDialog,
    setShowDialog,
    formData,
    setFormData,
    error,
    setError
}) {
    const dispatch = useDispatch();
    const toast = useRef(null); // useRef로 참조 생성

    useEffect(() => {
        if (!showDialog) {
            setFormData({ email: '', password: '' });
            setError('');
        }
    }, [showDialog, setFormData, setError]);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        const success = await dispatch(login(formData.email, formData.password));
        if (success) {
            // 성공 시 Toast 메시지 표시
            toast.current.show({
                severity: 'success',
                summary: '로그인 성공',
                detail: '성공적으로 로그인했습니다.',
                life: 3000,
            });
            setShowDialog(false);
        } else {
            // 실패 시 Toast 메시지 표시
            toast.current.show({
                severity: 'error',
                summary: '로그인 실패',
                detail: '이메일 또는 비밀번호가 올바르지 않습니다.',
                life: 3000,
            });
            setError('Invalid email or password');
        }
    };

    // const showSuccess = () => {
    //     toast.current.show({
    //         severity: 'Success', // 성공 메시지 스타일
    //         summary: 'Success',
    //         detail: 'Operation completed successfully',
    //         life: 3000, // 메시지 표시 시간 (ms)
    //     });
    // };

    // const showError = () => {
    //     toast.current.show({
    //         severity: 'Error', // 에러 메시지 스타일
    //         summary: 'Error',
    //         detail: 'Something went wrong',
    //         life: 3000,
    //     });
    // };

    const dialogFooter = (
        <div className="form-controls">
            <Toast ref={toast}/>
            <Button label="로그인" onClick={handleLogin} autoFocus />
            <p style={{ fontSize: '12px', textAlign: 'center', marginTop: '1rem' }}>
                <a href="/find-username" style={{ marginRight: '10px', color: '#3D6094' }}>아이디 찾기</a>
                <a href="/reset-password" style={{ color: '#3D6094' }}>비밀번호 찾기</a>
            </p>
        </div>
    );

    return (
        <Dialog
            header="로그인"
            visible={showDialog}
            className="custom-dialog"
            footer={dialogFooter}
            modal
            onHide={() => setShowDialog(false)}
        >
            <form onSubmit={handleLogin} className="login-form-container">
                <div className="form-group">
                    <label htmlFor="email">이메일</label>
                    <input
                        type="email"
                        name="email"
                        id="email"
                        placeholder="이메일"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="password">비밀번호</label>
                    <input
                        type="password"
                        name="password"
                        id="password"
                        placeholder="비밀번호"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>

                {error && <p className="error-message">{error}</p>}
            </form>
        </Dialog>
    );
}

export default LoginPage;
