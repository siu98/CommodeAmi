// import { createSlice } from '@reduxjs/toolkit';
// import axios from 'axios';
// import { jwtDecode } from 'jwt-decode';


// const initialState = {
//     accessToken: null,
//     user: null,
//     isInitialized: false,
//     isAuthenticated: false, // 초기값 false
// };

// const authSlice = createSlice({
//     name: 'auth',
//     initialState,
//     reducers: {
//         setAccessToken(state, action) {
//             state.accessToken = action.payload;
//             if (action.payload) {
//                 axios.defaults.headers.common['Authorization'] = `Bearer ${action.payload}`;
//                 const decoded = jwtDecode(action.payload);

//                 // 토큰 만료 체크
//                 if (decoded.exp * 1000 < Date.now()) {
//                     state.user = null;
//                     state.isInitialized = false;
//                     state.isAuthenticated = false; // 추가
//                 } else {
//                     state.user = {
//                         email: decoded.sub,
//                         userName: decoded.userName,
//                         profilePhoto: decoded.profilePhoto,
//                         userId: decoded.userId,
//                         userRole: decoded.auth,
//                     };
//                     state.isInitialized = true;
//                     state.isAuthenticated = true; // 추가
//                 }
//             } else {
//                 delete axios.defaults.headers.common['Authorization'];
//                 state.user = null;
//                 state.isInitialized = false;
//                 state.isAuthenticated = false; // 추가
//             }
//         },
//         logout(state) {
//             state.accessToken = null;
//             state.user = null;
//             state.isInitialized = false;
//             delete axios.defaults.headers.common['Authorization'];
//         },
//     },
// });

// export const { setAccessToken, logout } = authSlice.actions;

// export const login = (email, password) => async (dispatch) => {
//     try {
//         const response = await axios.post(
//             '/api/user/login',
//             { email, password },
//             { withCredentials: true }
//         );

//         const accessToken = response.headers['authorization']?.replace('Bearer ', '');
//         if (!accessToken) throw new Error('No access token received');

//         dispatch(setAccessToken(accessToken));
//         return true;
//     } catch (error) {
//         console.error('Login failed:', error);
//         dispatch(logout());
//         return false;
//     }
// };

// export const initializeAuth = () => async (dispatch) => {
//     try {
//         const response = await axios.get('/user/refresh', { withCredentials: true });

//         const newAccessToken = response.headers['authorization']?.replace('Bearer ', '');
//         if (newAccessToken) {
//             dispatch(setAccessToken(newAccessToken));
//             return true;
//         }
//         return false;
//     } catch (error) {
//         console.error('Auth initialization failed:', error);
//         dispatch(logout());
//         return false;
//     }
// };

// export default authSlice.reducer;

import { createSlice } from '@reduxjs/toolkit';
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

                // 토큰 만료 체크
                if (decoded.exp * 1000 < Date.now()) {
                    state.user = null;
                    state.isInitialized = false;
                    state.isAuthenticated = false;

                    // 로컬 스토리지 초기화
                    localStorage.removeItem('accessToken');
                    localStorage.removeItem('user');
                } else {
                    state.user = {
                        email: decoded.sub,
                        userName: decoded.userName,
                        profilePhoto: decoded.profilePhoto,
                        userId: decoded.userId,
                        userRole: decoded.auth,
                    };
                    state.isInitialized = true;
                    state.isAuthenticated = true;

                    // 로컬 스토리지에 저장
                    localStorage.setItem('accessToken', action.payload);
                    localStorage.setItem('user', JSON.stringify(state.user));
                }
            } else {
                delete axios.defaults.headers.common['Authorization'];
                state.user = null;
                state.isInitialized = false;
                state.isAuthenticated = false;

                // 로컬 스토리지 초기화
                localStorage.removeItem('accessToken');
                localStorage.removeItem('user');
            }
        },
        logout(state) {
            state.accessToken = null;
            state.user = null;
            state.isInitialized = false;
            state.isAuthenticated = false;

            // 로컬 스토리지 초기화
            localStorage.removeItem('accessToken');
            localStorage.removeItem('user');
        },
    },
});

export const { setAccessToken, logout } = authSlice.actions;

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
