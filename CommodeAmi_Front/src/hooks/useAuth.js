import { useSelector, useDispatch } from 'react-redux';
import { setAccessToken, logout } from '../store/slices/authSlice';

export const useAuth = () => {
    const dispatch = useDispatch();
    const { accessToken, user, isAuthenticated } = useSelector((state) => state.auth);

    const login = (token) => {
        dispatch(setAccessToken(token)); // 토큰 저장 및 상태 업데이트
    };

    const performLogout = () => {
        dispatch(logout());
    };

    return {
        isLoggedIn: isAuthenticated, 
        user,          
        accessToken,     
        login,           
        logout: performLogout, 
    };
};
