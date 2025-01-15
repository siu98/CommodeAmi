import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import FileUploadComponent from '../../components/FileUploadComponent'; 
import axios from 'axios';
import './CustomTicket.css'

const CustomTicket = () => {
    const navigate = useNavigate();
    const [customTickets, setCustomTickets] = useState([]);
    const [showDialog, setShowDialog] = useState(false); // Dialog 표시 상태
    const [isFlipped, setIsFlipped] = useState([]);
    // const user = useSelector((state) => state.auth.user);
    const { accessToken, user } = useSelector((state) => state.auth);
    const userId = user?.userId;
    // console.log("Redux state.auth.user:", user);
    // console.log("accessToken 확인: ", accessToken);
    // console.log("커스텀 티켓에서 userId 찍기: ", userId);
    useEffect(() => {
        fetchCustomTickets();
    }, []);

    const fetchCustomTickets = async () => {
        if(!userId) {
            console.log("userId가 없습니다.");
            return;
        }
        try {
            const response = await axios.get(`/api/customticket/${userId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });
            console.log('API 응답 데이터:', response.data);
            setCustomTickets(response.data.data);
            setIsFlipped(new Array(response.data.length).fill(false));
        } catch (error) {
            console.error("API request failed: ", error);
        }
    };

    const flipCard = (index) => {
        setIsFlipped((prev) => {
            const newFlipped = [...prev];
            newFlipped[index] = !newFlipped[index];
            return newFlipped;
        });
    };

    const handleMouseMove = (e, index) => {
        const container = e.target.closest('.container');
        if (!container) return;
    
        const bounds = container.getBoundingClientRect();
        const x = e.clientX - bounds.left;
        const y = e.clientY - bounds.top;
    
        if (x >= 0 && y >= 0 && x <= bounds.width && y <= bounds.height) {
            const backgroundX = (x / bounds.width) * 100;
            const backgroundY = (y / bounds.height) * 100;
    
            const overlay = container.querySelector('.overlay1');
            if (overlay) {
                overlay.style.background = `linear-gradient(105deg, 
                transparent 40%, 
                ${customTickets[index].hologram_color1} 45%, 
                ${customTickets[index].hologram_color2} 50%, 
                transparent 54%)`;
                overlay.style.backgroundSize = '200% 200%';
                overlay.style.backgroundPosition = `${backgroundX}% ${backgroundY}%`;
                overlay.style.filter = `opacity(${x / bounds.width}) brightness(1.2)`;
            }
    
            const rotateY = -1 / 5 * x + 20;
            const rotateX = 4 / 30 * y - 20;
            container.style.transform = `perspective(350px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
        }
    };

    const handleMouseOut = (e) => {
        const container = e.target.closest('.container');
        if (!container) 
            return;
    
        const overlay = container.querySelector('.overlay1');
        if (overlay) {
            overlay.style.filter = 'opacity(0)';
        }
    
        container.style.transform = 'perspective(350px) rotateY(0deg) rotateX(0deg)';
    };

        
    const getHologramStyle = (color1, color2) => {
        return `linear-gradient(105deg, transparent 40%, ${color1} 45%, ${color2} 50%, transparent 54%)`;
    };

    const deleteTicket = async (customTicketId) => {
        if (!window.confirm('정말로 이 티켓을 삭제하시겠습니까?')) return;
    
        try {
            const response = await axios.delete(`/api/customticket/${customTicketId}/${userId}`, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
                },
            });
    
            if (response.status === 200) {
                alert('티켓이 성공적으로 삭제되었습니다.');
                fetchCustomTickets();
                } else {
                    alert('티켓 삭제에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error deleting ticket:', error);
        }
    };


    return (
        <div>
            <Button 
                onClick={() => setShowDialog(true)} // Dialog를 열도록 설정
                className="create-btn"
                label="티켓 만들기"
            />
            <Dialog
                header="티켓 만들기"
                visible={showDialog}
                style={{ width: '50vw' }}
                onHide={() => setShowDialog(false)} // Dialog 닫기
            >
                <FileUploadComponent onClose={() => setShowDialog(false)} />
            </Dialog>
            <div className="ticket-list">
                {customTickets.map((ticket, index) => (
                <div
                    key={`${ticket.customTicketId}-${index}`} // 고유한 key로 생성
                    className="ticket-card"
                    onMouseMove={(e) => handleMouseMove(e, index)}
                    onMouseOut={(e) => handleMouseOut(e, index)}
                    onClick={() => flipCard(index)}
                    >
                    <div className={`flipper ${isFlipped[index] ? 'flipped' : ''}`}>
                        <div className="front">
                            <div className="container">
                                <div
                                    className="overlay1" 
                                    style={{ background: getHologramStyle(ticket.hologram_color1, ticket.hologram_color2) }}
                                ></div>
                                <div
                                    className="card-image"
                                    style={{ backgroundImage: `url(${ticket.ticket_image})` }}   
                                ></div>
                            </div>
                        </div>
                        <div className="back">
                            <div className="back-content">
                                <p>{ticket.comment || '코멘트가 없습니다. 코멘트를 추가해주세요.'}</p>
                                <div className="card-actions">
                                    <div className="top-right-buttons">
                                        <Button
                                            className="delete-btn"
                                            label="삭제"
                                            onClick={(e) => {
                                            e.stopPropagation();
                                            deleteTicket(ticket.custom_ticket_id);
                                            }}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                ))}
            </div>
        </div>
    );
};

export default CustomTicket;