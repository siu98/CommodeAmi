import axios from 'axios';

export const searchMovies = async (title) => {
    try {
        const token = localStorage.getItem('accessToken');
        const response = await axios.get(`/api/movie/search`, {
            params: { title },
            headers: {
                Authorization: `Bearer ${token}`, // 인증 토큰 추가
            },
        });
        return response.data.data;
    } catch {
        console.log("일치하는 영화가 없습니다.");
    }

};