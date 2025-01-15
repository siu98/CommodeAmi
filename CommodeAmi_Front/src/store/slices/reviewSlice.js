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
        
        const response = await axios.get(`/api/review/${userId}`)
    }
}