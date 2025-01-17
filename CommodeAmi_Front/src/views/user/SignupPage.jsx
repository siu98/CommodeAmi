import React, { useState, useEffect, useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { Toast } from 'primereact/toast';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import BirthdateSelector from '../../components/BirthdaySelector';
import axios from 'axios';
import './SignupPage.css';

function SignupPage({ showDialog, setShowDialog }) {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const toast = useRef(null); 
    const authState = useSelector((state) => state.auth);
    const [formData, setFormData] = useState({
        user_name: '',
        email: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        gender: ''
    })

    const [passwordMatch, setPasswordMatch] = useState(true);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [birth_date, setBirthdate] = useState({ year: '', month: '', day: '' });
    const [loading, setLoading] = useState(false);

    // useEffect(() => {
    //     console.log("현재 인증 상태:", authState);
    // }, [authState]);

    useEffect(() => {
        if (!showDialog) {
            setFormData({
                user_name: '',
                email: '',
                password: '',
                confirmPassword: '',
                nickname: '',
                gender: '',
            });
            setBirthdate({year: '', month: '', day: ''});    
        }
    }, [showDialog]);

    useEffect(() => {
        // 비밀번호 일치여부 확인
        setPasswordMatch(formData.password === formData.confirmPassword);
    }, [formData.password, formData.confirmPassword]);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    // const handleBirthdateChange = (field, value) => {
    //     setBirthdate({
    //         ...birthdate,
    //         [field]: value,
    //     });
    // };

    // const handleDateChange = (selectedDate) => {
    //     setBirthdate(selectedDate);
    //     console.log('Selected Birthdate:', selectedDate);
    // };

    const handleDateChange = (selectedDate) => {
        if (selectedDate) {
            const formattedDate = selectedDate.toISOString().split('T')[0]; // yyyy-mm-dd 형식
            setBirthdate(formattedDate);
        }
        console.log('Selected Birthdate:', selectedDate);
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

        // const fullBirthdate = `${birth_date.year}-${birth_date.month}-${birth_date.day}`;
        // const { confirmPassword, ...signupData } = { ...formData, birth_date: fullBirthdate };
        
        const { confirmPassword, ...signupData } = { ...formData, birth_date };

        try {
            setLoading(true);
            const response = await axios.post('/api/user/regist', signupData); // 회원가입 API 호출
            const a = response.data.data;
            toast.current.show({
                severity: 'success',
                summary: '회원가입 성공',
                detail: '회원가입이 성공적으로 완료되었습니다!',
                life: 3000,
            });
            console.log(a);
            setShowDialog(false);
            navigate("/");
        } catch (error) {
            const errorMessage = error.response?.data || '회원가입에 실패했습니다. 다시 시도해주세요.';
            toast.current.show({
                severity: 'error',
                summary: '회원가입 실패',
                detail: errorMessage,
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
            <Toast ref={toast} position="top-center" />
            <Dialog
                header="회원 가입"
                visible={showDialog}
                className="custom-dialog"
                footer={dialogFooter}
                modal
                onHide={() => setShowDialog(false)}
            >
                <form onSubmit={handleSubmit} className="signup-form-container">
                    {/* <div className="form-group">
                        <label htmlFor="username">아이디</label>
                        <input
                            type="text"
                            name="username"
                            id="username"
                            placeholder="아이디"
                            value={formData.username}
                            onChange={handleChange}
                            required
                        />
                    </div> */}

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
                        <p className={passwordMatch ? "success-message" : "error-message"}>
                            {passwordMatch ? "비밀번호가 일치합니다." : "비밀번호가 일치하지 않습니다."}
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
                        {/* <BirthdateSelector
                            year={birthdate.year}
                            month={birthdate.month}
                            day={birthdate.day}
                            handleYearChange={(value) => handleBirthdateChange('year', value)}
                            handleMonthChange={(value) => handleBirthdateChange('month', value)}
                            handleDayChange={(value) => handleBirthdateChange('day', value)}
                        /> */}
                    </div>
                </form>
            </Dialog>
        </>
    );
}

export default SignupPage;