import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { fetchUserReviews } from '../../api/reviews';
import './ReviewsPage.css';

const ReviewsPage = () => {
    const { accessToken, user } = useSelector((state) => state.auth); // Redux 상태에서 인증 정보 가져오기
    const [reviews, setReviews] = useState([]);

    useEffect(() => {
        const getReviews = async () => {
            if (!user?.userId || !accessToken) {
                setError("로그인 상태를 확인하세요.");
                return;
            }

            try {
                const userReviews = await fetchUserReviews(user.userId, accessToken);
                const reviewsArray = Array.isArray(userReviews) ? userReviews : Object.values(userReviews); // 배열로 변환
                setReviews(reviewsArray); // 가져온 리뷰 데이터를 상태에 저장
            } catch (err) {
                console.error("리뷰를 가져오는 중 오류 발생:", err);
                setError("리뷰를 가져오지 못했습니다.");
            }
        };

        getReviews();
    }, [user?.userId, accessToken]);


return (
  <div>
    <div className="profile-review-container">
      {reviews.map((review, index) => (
        <div key={index} className="profile-review-item">
  <div className="review-header">
    <h4>{review.title}</h4>
    {/* <p>{review.review}</p> */}
    <p className="date-watched">{review.watched_at ? `관람일자: ${review.watched_at}` : '관람일자 없음'}</p>
  </div>
  <p>{review.review}</p>
</div>
      ))}
    </div>
  </div>

);
};

export default ReviewsPage;