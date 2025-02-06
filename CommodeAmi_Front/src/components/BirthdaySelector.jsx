import React, { useState } from 'react';
import { Calendar } from 'primereact/calendar';
import './BirthdaySelector.css'

function BirthdateSelector({ selectedDate, handleDateChange }) {

    const [date, setDate] = useState(selectedDate || null);

    const onDateChange = (e) => {
        const selected = e.value;
        setDate(selected);
        handleDateChange(selected); 
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
