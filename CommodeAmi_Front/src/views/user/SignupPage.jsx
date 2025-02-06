import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { Toast } from 'primereact/toast';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import BirthdateSelector from '../../components/BirthdaySelector';
import axios from 'axios';
import './SignupPage.css';

function SignupPage({ showDialog, setShowDialog }) {
    const navigate = useNavigate();
    const toast = useRef(null);

    const [formData, setFormData] = useState({
        user_name: '',
        email: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        gender: ''
    });
    const [passwordMatch, setPasswordMatch] = useState(true);
    const [birth_date, setBirthdate] = useState('');
    const [loading, setLoading] = useState(false);

    // 이메일 인증 관련 상태
    const [isEmailVerified, setIsEmailVerified] = useState(false);
    const [verificationCode, setVerificationCode] = useState('');
    const [isCodeSent, setIsCodeSent] = useState(false);

    useEffect(() => {
        if (!showDialog) {
            resetForm();
        }
    }, [showDialog]);

    useEffect(() => {
        // 비밀번호 일치 여부 확인
        setPasswordMatch(formData.password === formData.confirmPassword);
    }, [formData.password, formData.confirmPassword]);

    const resetForm = () => {
        setFormData({
            user_name: '',
            email: '',
            password: '',
            confirmPassword: '',
            nickname: '',
            gender: ''
        });
        setBirthdate('');
        setIsEmailVerified(false);
        setVerificationCode('');
        setIsCodeSent(false);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleDateChange = (selectedDate) => {
        if (selectedDate) {
            const formattedDate = selectedDate.toISOString().split('T')[0];
            setBirthdate(formattedDate);
        }
    };

    const sendVerificationCode = async () => {
        console.log("formData에서 이메일 확인: ", formData);
        if (!formData.email) {
            toast.current.show({
                severity: 'error',
                summary: '전송 실패',
                detail: '이메일을 입력해주세요.',
                life: 3000,
            });
            return;
        }

        try {
            setLoading(true);
            await axios.post('/api/user/send-verification', null, { params: { email: formData.email }});
            setIsCodeSent(true);
            toast.current.show({
                severity: 'success',
                summary: '인증 코드 전송',
                detail: '인증 코드가 이메일로 전송되었습니다.',
                life: 3000,
            });
        } catch (error) {
            toast.current.show({
                severity: 'error',
                summary: '전송 실패',
                detail: error.response?.data?.message || '인증 코드 전송에 실패했습니다.',
                life: 3000,
            });
        } finally {
            setLoading(false);
        }
    };

    const verifyCode = async () => {
        if (!verificationCode) {
            toast.current.show({
                severity: 'error',
                summary: '인증 실패',
                detail: '인증 코드를 입력해주세요.',
                life: 3000,
            });
            return;
        }

        try {
            const response = await axios.post('/api/user/verify-code', null, {
                // email: formData.email,
                // code: verificationCode,
                params: { email: formData.email, code: verificationCode }
            });
            if (response.data.success) {
                setIsEmailVerified(true);
                toast.current.show({
                    severity: 'success',
                    summary: '인증 성공',
                    detail: '이메일 인증이 완료되었습니다.',
                    life: 3000,
                });
            } else {
                toast.current.show({
                    severity: 'error',
                    summary: '인증 실패',
                    detail: '인증 코드가 올바르지 않습니다.',
                    life: 3000,
                });
            }
        } catch (error) {
            toast.current.show({
                severity: 'error',
                summary: '인증 실패',
                detail: error.response?.data?.message || '인증에 실패했습니다.',
                life: 3000,
            });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!passwordMatch) {
            toast.current.show({
                severity: 'error',
                summary: '회원가입 실패',
                detail: '비밀번호가 일치하지 않습니다.',
                life: 3000,
            });
            return;
        }

        if (!isEmailVerified) {
            toast.current.show({
                severity: 'error',
                summary: '회원가입 실패',
                detail: '이메일 인증을 완료해주세요.',
                life: 3000,
            });
            return;
        }

        const { confirmPassword, ...signupData } = { ...formData, birth_date };

        try {
            setLoading(true);
            await axios.post('/api/user/regist', signupData);
            toast.current.show({
                severity: 'success',
                summary: '회원가입 성공',
                detail: '회원가입이 성공적으로 완료되었습니다!',
                life: 3000,
            });
            setShowDialog(false);
            navigate('/');
        } catch (error) {
            toast.current.show({
                severity: 'error',
                summary: '회원가입 실패',
                detail: error.response?.data || '회원가입에 실패했습니다. 다시 시도해주세요.',
                life: 3000,
            });
        } finally {
            setLoading(false);
        }
    };

    const dialogFooter = (
        <div className="form-controls">
            <Button label="회원가입" onClick={handleSubmit} loading={loading} autoFocus />
        </div>
    );

    return (
        <>
            <Toast ref={toast} position="top-right" />
            <Dialog
                header="회원 가입"
                visible={showDialog}
                className="custom-dialog"
                footer={dialogFooter}
                modal
                onHide={() => setShowDialog(false)}
            >
                <form onSubmit={handleSubmit} className="signup-form-container">
                    <div className="form-group">
                        <label htmlFor="user_name">이름</label>
                        <input
                            type="text"
                            name="user_name"
                            id="user_name"
                            placeholder="이름"
                            value={formData.user_name}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">이메일</label>
                        <div style={{ display: 'flex', gap: '10px' }}>
                            <input
                                type="email"
                                name="email"
                                id="email"
                                placeholder="이메일"
                                value={formData.email}
                                onChange={handleChange}
                                required
                                disabled={isCodeSent} // 인증 코드 요청 후 이메일 변경 불가
                            />
                            <Button label="인증 요청" onClick={sendVerificationCode} disabled={isCodeSent} />
                        </div>
                    </div>

                    {isCodeSent && (
                        <div className="form-group">
                            <label htmlFor="verificationCode">인증 코드</label>
                            <div style={{ display: 'flex', gap: '10px' }}>
                                <input
                                    type="text"
                                    id="verificationCode"
                                    placeholder="인증"
                                    value={verificationCode}
                                    onChange={(e) => setVerificationCode(e.target.value)}
                                    disabled={isEmailVerified}
                                />
                                <Button label="인증" onClick={verifyCode} disabled={isEmailVerified} />
                            </div>
                        </div>
                    )}

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

                    <div className="form-group">
                        <label htmlFor="confirmPassword">비밀번호 확인</label>
                        <input
                            type="password"
                            name="confirmPassword"
                            id="confirmPassword"
                            placeholder="비밀번호 확인"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            required
                        />
                        {formData.confirmPassword && (
                            <p className={passwordMatch ? 'success-message' : 'error-message'}>
                                {passwordMatch ? '비밀번호가 일치합니다.' : '비밀번호가 일치하지 않습니다.'}
                            </p>
                        )}
                    </div>

                    <div className="form-group">
                        <label htmlFor="nickname">닉네임</label>
                        <input
                            type="text"
                            name="nickname"
                            id="nickname"
                            placeholder="닉네임"
                            value={formData.nickname}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="gender">성별</label>
                        <select name="gender" id="gender" onChange={handleChange} required>
                            <option value="">선택하세요</option>
                            <option value="MALE">남성</option>
                            <option value="FEMALE">여성</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="birth_date">생년월일</label>
                        <BirthdateSelector 
                            selectedDate={birth_date} 
                            handleDateChange={handleDateChange} 
                        />
                    </div>
                </form>
            </Dialog>
        </>
    );
}

export default SignupPage;
