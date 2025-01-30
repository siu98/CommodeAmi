// import { createSlice } from '@reduxjs/toolkit';
// import axios from 'axios';

// const initialState = {
//     scope: {}, // { movieId: scope }
//     loading: false,
//     error: null,
// };

// const scopeSlice = createSlice({
//     name: 'scope',
//     initialState,
//     reducers: {
//         setScope(state, action) {
//             const { movieId, scope, scopeId } = action.payload;
//             state.scope[movieId] = { movieId, scope, scopeId }; // 특정 영화의 별점 추가 또는 업데이트
//         },
//         setLoading(state, action) {
//             state.loading = action.payload;
//         },
//         setError(state, action) {
//             state.error = action.payload;
//         },
//         resetScope(state) {
//             return initialState; // scope 상태 초기화
//         },
//     },
// });

// export const { setScope, setLoading, setError, resetScope } = scopeSlice.actions;

// export const fetchMovieScope = (userId, movieId) => async (dispatch, getState) => {
//     const { accessToken } = getState().auth;

//     if (!accessToken) {
//         console.warn("Access token is missing. Skipping fetchMovieScope.");
//         return; // 액세스 토큰 없으면 API 호출하지 않음
//     }

//     const scope = response?.data?.data?.scope || 0;
//     const scopeId = response.data.data.scope_id;

//     if (!scope) {
//         return;
//     }


//     try {
//         dispatch(setLoading(true));

//         const response = await axios.get(`/api/scope/${userId}/${movieId}`, {
//             headers: {
//                 Authorization: `Bearer ${accessToken}`,
//             },
//         });


//         console.log("response.data 확인: ", response.data);

//         console.log("API 응답 받은 scope 값:", scope);

//         // Redux 상태 업데이트
//         dispatch(setScope({ movieId, scope, scopeId }));
//     } catch (error) {
//         console.error("Failed to fetch movie scope:", error.message || error);

//         // 기본값으로 처리
//         dispatch(setScope({ movieId, scope: 0 }));
//         dispatch(setError(error.message));
//     } finally {
//         dispatch(setLoading(false));
//     }
// };

// export const fetchUserScopes = () => async (dispatch, getState) => {
//     const { accessToken, user } = getState().auth; // Redux 상태에서 user와 accessToken 가져오기

//     if (!accessToken || !user?.userId) {
//         console.warn("Access token or userId is missing. Skipping fetchUserScopes.");
//         return; // 액세스 토큰 또는 userId가 없으면 API 호출하지 않음
//     }

//     const userId = user.userId; // Redux 상태에서 userId 가져오기

//     try {
//         // 별점 데이터를 가져오기 위한 API 호출
//         const response = await axios.get(`/api/scope/user/${userId}`, {
//             headers: {
//                 Authorization: `Bearer ${accessToken}`,
//             },
//         });

//         const scopes = response?.data?.data || {}; // { movieId: 별점 } 형태
//         console.log("사용자 별점 데이터:", scopes);

//         // Redux 상태 업데이트
//         // Object.entries(scopes).forEach(([movieId, scope]) => {
//         //     dispatch(setScope({ movieId, scope }));
//         // });
//         Object.entries(scopes).forEach(([movieId, data]) => {
//             const { scope, scope_id: scopeId } = data; // 응답에서 scope와 scopeId를 분리
//             dispatch(setScope({ movieId, scope, scopeId })); // scope와 scopeId를 Redux에 저장
//         });
//     } catch (error) {
//         console.error("Failed to fetch user scopes:", error.message || error);
//     }
// };


// export default scopeSlice.reducer;

import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';

const initialState = {
    scope: {}, // { movieId: scope }
    loading: false,
    error: null,
};

const scopeSlice = createSlice({
    name: 'scope',
    initialState,
    reducers: {
        setScope(state, action) {
            const { movieId, scope } = action.payload;
            state.scope[movieId] = scope; // 특정 영화의 별점 추가 또는 업데이트
        },
        setLoading(state, action) {
            state.loading = action.payload;
        },
        setError(state, action) {
            state.error = action.payload;
        },
        resetScope(state) {
            return initialState; // scope 상태 초기화
        },
    },
});

export const { setScope, setLoading, setError, resetScope } = scopeSlice.actions;

export const fetchMovieScope = (userId, movieId) => async (dispatch, getState) => {
    const { accessToken } = getState().auth;

    if (!accessToken) {
        console.warn("Access token is missing. Skipping fetchMovieScope.");
        return; // 액세스 토큰 없으면 API 호출하지 않음
    }

    try {
        dispatch(setLoading(true));

        const response = await axios.get(`/api/scope/${userId}/${movieId}`, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        const scope = response?.data?.data?.scope || 0;
        console.log("API 응답 받은 scope 값:", scope);

        // Redux 상태 업데이트
        dispatch(setScope({ movieId, scope }));
    } catch (error) {
        console.error("Failed to fetch movie scope:", error.message || error);

        // 기본값으로 처리
        dispatch(setScope({ movieId, scope: 0 }));
        dispatch(setError(error.message));
    } finally {
        dispatch(setLoading(false));
    }
};

export const fetchUserScopes = () => async (dispatch, getState) => {
    const { accessToken, user } = getState().auth; // Redux 상태에서 user와 accessToken 가져오기

    if (!accessToken || !user?.userId) {
        console.warn("Access token or userId is missing. Skipping fetchUserScopes.");
        return; // 액세스 토큰 또는 userId가 없으면 API 호출하지 않음
    }

    const userId = user.userId; // Redux 상태에서 userId 가져오기

    try {
        // 별점 데이터를 가져오기 위한 API 호출
        const response = await axios.get(`/api/scope/user/${userId}`, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        const scopes = response?.data?.data || {}; // { movieId: 별점 } 형태
        console.log("사용자 별점 데이터:", scopes);

        // Redux 상태 업데이트
        Object.entries(scopes).forEach(([movieId, scope]) => {
            dispatch(setScope({ movieId, scope }));
        });
    } catch (error) {
        console.error("Failed to fetch user scopes:", error.message || error);
    }
};


export default scopeSlice.reducer;