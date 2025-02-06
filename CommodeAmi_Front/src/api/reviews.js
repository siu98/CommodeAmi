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
        return movieReviews;
    } catch (error) {
        console.error("Failed to fetch movie reviews:", error.message || error);
        throw error; 
    }
};

export const fetchUserReviews = async (userId, accessToken) => {
    if (!accessToken || !userId) {
        console.warn("Access token or userId is missing. Skipping fetchUserReviews.");
        return null; 
    }

    try {
        const response = await axios.get(`/api/review/user/${userId}`, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        const userReviews = response?.data?.data || {};
        console.log("사용자 리뷰 데이터:", userReviews);
        return userReviews; 
    } catch (error) {
        console.error("Failed to fetch user reviews:", error.message || error);
        throw error; 
    }
};
