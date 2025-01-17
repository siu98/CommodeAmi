import axios from 'axios';

export const fetchReviewByMovieId = async (movieId, accessToken) => {
    try {
        const response = await axios.get(`/api/review/movie/${movieId}`, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        const movieReviews = response?.data?.data || {};
        console.log("해당 영화의 리뷰 조회: ", movieReviews);
        return movieReviews; // 데이터를 반환
    } catch (error) {
        console.error("Failed to fetch movie reviews:", error.message || error);
        throw error; // 오류를 호출자에게 전달
    }
};

export const fetchUserReviews = async (userId, accessToken) => {
    if (!accessToken || !userId) {
        console.warn("Access token or userId is missing. Skipping fetchUserReviews.");
        return null; // 액세스 토큰 또는 userId가 없으면 null 반환
    }

    try {
        const response = await axios.get(`/api/review/user/${userId}`, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        const userReviews = response?.data?.data || {};
        console.log("사용자 리뷰 데이터:", userReviews);
        return userReviews; // 데이터를 반환
    } catch (error) {
        console.error("Failed to fetch user reviews:", error.message || error);
        throw error; // 호출자에게 오류 전달
    }
};
