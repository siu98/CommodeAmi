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
    const [showDialog, setShowDialog] = useState(false); 
    const [showSignupDialog, setShowSignupDialog] = useState(false); 
    const [showPasswordDialog, setShowPasswordDialog] = useState(false); 
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [formData, setFormData] = useState({ email: '', password: '' }); 
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
        const response = await axios.put(`/api/user/password/${userId}`, {
            currentPwd: currentPassword,
            newPwd: newPassword,
        });

        console.log(response.data);

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
        setTimeout(() => setValue(''), 0);
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
                                e.preventDefault(); 
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

            <LoginPage
                showDialog={showDialog}
                setShowDialog={setShowDialog}
                formData={formData}
                setFormData={setFormData}
                error={error}
                setError={setError}
            />

            <SignupPage
                showDialog={showSignupDialog}
                setShowDialog={setShowSignupDialog}
                formData={signupFormData}
                setFormData={setSignupFormData}
            />

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
