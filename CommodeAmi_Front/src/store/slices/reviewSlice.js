import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';

const initialState = {
    review: {},
    loading: false,
    error: null,
};

const reviewSlice = createSlice({
    name: 'review',
    initialState,
    reducers: {
        setReview(state, action) {
            const { movieId, review } = action.payload;
            state.review[movieId] = review;
        },
        setLoading(state, action) {
            state.loading = action.payload;
        },
        setError(state, action) {
            state.error = action.payload;
        },
        resetReview(state) {
            return initialState; // 리뷰 상태 초기화
        },
    },
});

export const { setReview, setLoading, setError, resetReview } = reviewSlice.actions;

export const fetchMovieReview = (userId, movieId) => async (dispatch, getState) => {
    const { accessToken } = getState().auth;

    if (!accessToken) {
        console.warn("Access token is missing. Skipping fetchMovieScope.");
        return; 
    }

    try {
        dispatch(setLoading(true));
        
        const response = await axios.get(`/api/review/${userId}/${movieId}`,
            {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            }
        );

        const review = response?.data?.data?.review || {};
        console.log("API 응답 받은 리뷰 데이터(reviewSlice): ", review);

        // Redux 상태 업데이트
        dispatch(setReview({ movieId, review }));
    } catch (error) {
        console.error("Failed to fetch movie reviews:", error.message || error);
    }
};

export const fetchUserReviews = () => async (dispatch, getState) => {
    const { accessToken, user } = getState().auth;

    if (!accessToken || !user?.userId) {
        console.warn("Access token or userId is missing. Skipping fetchUserReviews.");
        return; // 액세스 토큰 또는 userId가 없으면 API 호출하지 않음
    }

    const userId = user.userId;

    try {
        const response = await axios.get(`/api/review/${userId}`, 
            {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            }
        );

        const reviews = response?.data?.data || {};
        console.log("사용자 리뷰 데이터:", reviews);

        // Redux 상태 업데이트
        Object.entries(reviews).forEach(([movieId, review]) => {
            dispatch(setReview({ movieId, review }));
        });
    } catch (error) {
        console.error("Failed to fetch user reviews:", error.message || error);    
    }
}

export default reviewSlice.reducer;