import React, { useEffect, useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
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
    // Redux 상태에서 인증 정보 가져오기
    const authState = useSelector((state) => state.auth);
    useEffect(() => {
        console.log("현재 인증 상태:", authState);
    }, [authState]);
    
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
            console.log("로그인 결과:", success);
            console.log("인증 상태:", authState);
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
