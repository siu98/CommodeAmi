import { createSlice } from '@reduxjs/toolkit';
import { resetScope } from './scopeSlice'; // scopeSlice 파일 경로에 맞게 설정
import axios from 'axios';
import { jwtDecode }from 'jwt-decode';

const initialState = {
    accessToken: localStorage.getItem('accessToken') || null,
    user: JSON.parse(localStorage.getItem('user')) || null,
    isInitialized: !!localStorage.getItem('accessToken'),
    isAuthenticated: !!localStorage.getItem('accessToken'),
};

const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        setAccessToken(state, action) {
            state.accessToken = action.payload;
            if (action.payload) {
                axios.defaults.headers.common['Authorization'] = `Bearer ${action.payload}`;
                const decoded = jwtDecode(action.payload);

                if (decoded.exp * 1000 < Date.now()) {
                    state.user = null;
                    state.isInitialized = false;
                    state.isAuthenticated = false;

                    localStorage.removeItem('accessToken');
                    localStorage.removeItem('user');
                } else {
                    state.user = {
                        email: decoded.sub,
                        userName: decoded.userName,
                        userId: decoded.userid,
                        nickName: decoded.nickname,
                        userRole: decoded.auth,
                    };
                    state.isInitialized = true;
                    state.isAuthenticated = true;

                    localStorage.setItem('accessToken', action.payload);
                    localStorage.setItem('user', JSON.stringify(state.user));
                }
            } else {
                delete axios.defaults.headers.common['Authorization'];
                state.user = null;
                state.isInitialized = false;
                state.isAuthenticated = false;

                localStorage.removeItem('accessToken');
                localStorage.removeItem('user');
            }
        },
        resetState(state) {
            state.accessToken = null;
            state.user = null;
            state.isInitialized = false;
            state.isAuthenticated = false;
            localStorage.removeItem('accessToken');
            localStorage.removeItem('user');
        },
    },
});

export const { setAccessToken, resetState } = authSlice.actions;

// Thunk로 logout 정의
export const logout = () => (dispatch) => {
    dispatch(resetState()); // Redux 상태 초기화
    dispatch(resetScope()); // Scope 상태 초기화

    // 로컬 스토리지 초기화
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
};

export const login = (email, password) => async (dispatch) => {
    try {
        const response = await axios.post(
            '/api/user/login',
            { email, password },
            { withCredentials: true }
        );

        const accessToken = response.headers['authorization']?.replace('Bearer ', '');
        if (!accessToken) throw new Error('No access token received');

        dispatch(setAccessToken(accessToken));
        return true;
    } catch (error) {
        console.error('Login failed:', error);
        dispatch(logout());
        return false;
    }
};

export const initializeAuth = () => async (dispatch) => {
    try {
        const storedToken = localStorage.getItem('accessToken');
        if (storedToken) {
            dispatch(setAccessToken(storedToken));
            return true;
        }
        return false;
    } catch (error) {
        console.error('Auth initialization failed:', error);
        dispatch(logout());
        return false;
    }
};

export default authSlice.reducer;