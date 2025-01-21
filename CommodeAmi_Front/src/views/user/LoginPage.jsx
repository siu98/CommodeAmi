// import React, { useEffect, useRef } from 'react';
// import { useDispatch, useSelector } from 'react-redux';
// import { login } from '../../store/slices/authSlice';
// import { Dialog } from 'primereact/dialog';
// import { Button } from 'primereact/button';
// import { Toast } from 'primereact/toast';
// import './LoginPage.css';

// function LoginPage({
//     showDialog,
//     setShowDialog,
//     formData,
//     setFormData,
//     error,
//     setError
// }) {
//     const dispatch = useDispatch();
//     const toast = useRef(null); // useRef로 참조 생성
//     // Redux 상태에서 인증 정보 가져오기
//     const authState = useSelector((state) => state.auth);
//     useEffect(() => {
//         console.log("현재 인증 상태:", authState);
//     }, [authState]);
    
//     useEffect(() => {
//         if (!showDialog) {
//             setFormData({ email: '', password: '' });
//             setError('');
//         }
//     }, [showDialog, setFormData, setError]);

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
//             // 성공 시 Toast 메시지 표시
//             toast.current.show({
//                 severity: 'success',
//                 summary: '로그인 성공',
//                 detail: '성공적으로 로그인했습니다.',
//                 life: 3000,
//             });
//             console.log("로그인 결과:", success);
//             // console.log("인증 상태:", authState);
//             setShowDialog(false);
//         } else {
//             // 실패 시 Toast 메시지 표시
//             toast.current.show({
//                 severity: 'error',
//                 summary: '로그인 실패',
//                 detail: '이메일 또는 비밀번호가 올바르지 않습니다.',
//                 life: 3000,
//             });
//             setError('Invalid email or password');
//         }
//     };

//     const dialogFooter = (
//         <div className="form-controls">
//             <Toast ref={toast}/>
//             <Button label="로그인" onClick={handleLogin} autoFocus />
//             <p style={{ fontSize: '12px', textAlign: 'center', marginTop: '1rem' }}>
//                 <a href="/find-username" style={{ marginRight: '10px', color: '#3D6094' }}>아이디 찾기</a>
//                 <a href="/reset-password" style={{ color: '#3D6094' }}>비밀번호 찾기</a>
//             </p>
//         </div>
//     );

//     return (
//         <Dialog
//             header="로그인"
//             visible={showDialog}
//             className="custom-dialog"
//             footer={dialogFooter}
//             modal
//             onHide={() => setShowDialog(false)}
//         >
//             <form onSubmit={handleLogin} className="login-form-container">
//                 <div className="form-group">
//                     <label htmlFor="email">이메일</label>
//                     <input
//                         type="email"
//                         name="email"
//                         id="email"
//                         placeholder="이메일"
//                         value={formData.email}
//                         onChange={handleChange}
//                         required
//                     />
//                 </div>

//                 <div className="form-group">
//                     <label htmlFor="password">비밀번호</label>
//                     <input
//                         type="password"
//                         name="password"
//                         id="password"
//                         placeholder="비밀번호"
//                         value={formData.password}
//                         onChange={handleChange}
//                         required
//                     />
//                 </div>

//                 {error && <p className="error-message">{error}</p>}
//             </form>
//         </Dialog>
//     );
// }

// export default LoginPage;


import React, { useEffect, useRef, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { login } from '../../store/slices/authSlice';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { Toast } from 'primereact/toast';
import './LoginPage.css';
import axios from 'axios';

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
    const authState = useSelector((state) => state.auth);
    const [showPasswordResetDialog, setShowPasswordResetDialog] = useState(false);
    const [resetEmail, setResetEmail] = useState('');

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
            toast.current.show({
                severity: 'success',
                summary: '로그인 성공',
                detail: '성공적으로 로그인했습니다.',
                life: 3000,
            });
            setShowDialog(false);
        } else {
            toast.current.show({
                severity: 'error',
                summary: '로그인 실패',
                detail: '이메일 또는 비밀번호가 올바르지 않습니다.',
                life: 3000,
            });
            setError('Invalid email or password');
        }
    };

    const handlePasswordReset = async () => {
        if (!resetEmail.trim()) {
            toast.current?.show({
                severity: 'error',
                summary: '오류',
                detail: '이메일을 입력해주세요.',
                life: 3000,
            });
            return;
        }

        try {
            console.log("전송할 이메일:", resetEmail);
            // await axios.post('/api/user/reset', { email: resetEmail });
            await axios.post('/api/user/reset', null, {params: { email: resetEmail }});
            toast.current?.show({
                severity: 'success',
                summary: '성공',
                detail: '임시 비밀번호가 이메일로 전송되었습니다.',
                life: 3000,
            });
            setShowPasswordResetDialog(false);
            setTimeout(() => setShowDialog(true), 0); // 로그인 Dialog 다시 열기
        } catch (error) {
            toast.current?.show({
                severity: 'error',
                summary: '오류',
                detail: '이메일 전송에 실패했습니다. 이메일 주소를 확인해주세요.',
                life: 3000,
            });
        }
    };

    const loginDialogFooter = (
        <div className="form-controls">
            <Toast ref={toast} />
            <Button label="로그인" onClick={handleLogin} autoFocus />
            <p style={{ fontSize: '12px', textAlign: 'center', marginTop: '1rem' }}>
                <a href="/find-username" style={{ marginRight: '10px', color: '#3D6094' }}>
                    아이디 찾기
                </a>
                <span
                    style={{ color: '#3D6094', cursor: 'pointer' }}
                    onClick={() => {
                        setShowPasswordResetDialog(true);
                        setShowDialog(false);
                    }}
                >
                    비밀번호 찾기
                </span>
            </p>
        </div>
    );

    const passwordResetDialogFooter = (
        <div className="form-controls">
            <Button label="임시 비밀번호 전송" onClick={handlePasswordReset} />
        </div>
    );

    return (
        <>
            <Dialog
                header="로그인"
                visible={showDialog}
                className="custom-dialog"
                footer={loginDialogFooter}
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

            <Dialog
                header="비밀번호 찾기"
                visible={showPasswordResetDialog}
                className="custom-dialog"
                footer={passwordResetDialogFooter}
                modal
                onHide={() => setShowPasswordResetDialog(false)}
            >
                <div className="password-reset-container">
                    <p>비밀번호를 재설정하려면 이메일 주소를 입력해주세요.</p>
                    <div className="form-group2">
                        {/* <label className="reset-email">이메일</label> */}
                        <div style={{ display: 'flex', gap: '10px' }}>
                        <input
                            type="email"
                            name="reset-email"
                            className="reset-email"
                            placeholder="이메일"
                            value={resetEmail}
                            onChange={(e) => setResetEmail(e.target.value)}
                            required
                        />
                        </div>
                    </div>
                </div>
            </Dialog>
        </>
    );
}

export default LoginPage;
