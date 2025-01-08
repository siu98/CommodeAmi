import React from 'react';
import { FaStar, FaStarHalfAlt } from 'react-icons/fa';
import './StarDisplay.css'; // 기본 CSS

function StarDisplay({ rating, className }) {
  return (
    <div className={`star-display ${className || ''}`}> {/* 추가 클래스 적용 */}
      {Array(5)
        .fill(0)
        .map((_, idx) => {
          if (idx + 1 <= rating) {
            return <FaStar key={idx} size={50} color="gold" />;
          } else if (idx + 0.5 <= rating) {
            return <FaStarHalfAlt key={idx} size={50} color="gold" />;
          } else {
            return <FaStar key={idx} size={50} color="lightGray" />;
          }
        })}
    </div>
  );
}

export default StarDisplay;

