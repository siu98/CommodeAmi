import { configureStore } from '@reduxjs/toolkit';
import counterReducer from '../features/counterSlice';
import authReducer from './slices/authSlice';
import scopeReducer from './slices/scopeSlice';
import reviewReducer from './slices/reviewSlice';

const store = configureStore({
    reducer: {
        // 여기에 리듀서 추가 
        auth: authReducer,
        counter: counterReducer,
        scope: scopeReducer,
        review: reviewReducer,
    }
});

export default store;