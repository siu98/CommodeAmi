import React, { useState } from 'react';
import { Calendar } from 'primereact/calendar';
import './BirthdaySelector.css'

function BirthdateSelector({ selectedDate, handleDateChange }) {
    // 내부 상태 관리 (필요하면 사용)
    const [date, setDate] = useState(selectedDate || null);

    const onDateChange = (e) => {
        const selected = e.value;
        setDate(selected);
        handleDateChange(selected); // 부모 컴포넌트로 선택된 날짜 전달
    };

    return (
        <div className="birthdate-container">
            <Calendar 
                value={date} 
                onChange={onDateChange} 
                dateFormat="yy-mm-dd" 
                showIcon 
                placeholder="생년월일을 선택하세요" 
                monthNavigator 
                yearNavigator 
                yearRange="1900:2025"
            />
        </div>
    );
}

export default BirthdateSelector;
