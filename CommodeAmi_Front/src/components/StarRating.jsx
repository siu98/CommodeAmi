import React, { useState, useEffect } from 'react';
import { FaStar, FaStarHalfAlt } from 'react-icons/fa';
import './StarRating.css';

function StarRating({ onRatingSelect, initialRating }) {
  const [score, setScore] = useState(initialRating);
  const [scoreFixed, setScoreFixed] = useState(initialRating);

  const handleLeftHalfEnter = (idx) => setScore(idx + 0.5);

  const handleRightHalfEnter = (idx) => setScore(idx + 1);

  const handleStarClick = () => {
    setScoreFixed(score);
    onRatingSelect(score);
  };

  const handleStarLeave = () => {
    if (score !== scoreFixed) {
      setScore(scoreFixed);
    }
  };

  useEffect(() => {
    setScore(initialRating);
    setScoreFixed(initialRating);
  }, [initialRating]);

  return (
    <div className="row-box">
      {Array(5)
        .fill(0)
        .map((_, idx) => (
          <div className="star-div" key={idx} onClick={handleStarClick}>
            {score - Math.floor(score) === 0.5 && Math.floor(score) === idx ? (
              <FaStarHalfAlt
                // style={{ position: 'absolute' }}
                size={50}
                color="gold"
              />
            ) : idx + 1 > score ? (
              <FaStar
                // style={{ position: 'absolute' }}
                size={50}
                color="lightGray"
              />
            ) : (
              <FaStar
                // style={{ position: 'absolute' }}
                size={50}
                color="gold"
              />
            )}
            <div
              className="left"
              onMouseEnter={() => handleLeftHalfEnter(idx)}
              onMouseLeave={handleStarLeave}
            />
            <div
              className="right"
              onMouseEnter={() => handleRightHalfEnter(idx)}
              onMouseLeave={handleStarLeave}
            />
          </div>
        ))}
    </div>
  );
}

export default StarRating;
