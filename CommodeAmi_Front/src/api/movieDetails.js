import axios from 'axios';
import convertCountry from '../components/ConvertCountry'; 

export const fetchMovie = async (movieId) => {
    try {
        const response = await axios.get(`/api/movie/${movieId}`);
        const movieData = response.data;
        console.log(movieData); 
        // origin_country가 문자열로 오면 배열로 변환
        if (movieData.originalCountry) {
            let originCountries;
            try {
                if (typeof movieData.originalCountry === 'string') {
                    // 문자열일 경우 배열로 변환
                    originCountries = JSON.parse(movieData.originalCountry.replace(/'/g, '"'));
                } else if (Array.isArray(movieData.originalCountry)) {
                    originCountries = movieData.originalCountry;
                } else {
                    originCountries = [];
                }
            } catch (e) {
                console.error('Error parsing origin_country:', e);
                originCountries = [];
            }
            
            // 국가 코드 매핑
            movieData.originalCountry = originCountries.map(convertCountry);
        }
        return movieData;
    } catch (error) {
        console.error('Error fetching movie data:', error);
        throw error; // 에러 발생 시 호출한 곳에서 처리 가능하도록 에러 던짐
    }
};

// 배우 정보 API 호출
export const fetchActors = async () => {
    try {
        const response = await axios.get(`/api/actors/`);
        return response.data;
    } catch (error) {
        console.error('Error fetching actors:', error);
        throw error;
    }
};

// 영화 배우 정보 API 호출
export const fetchMovieActors = async () => {
    try {
        const response = await axios.get(`/api/movieactors/`);
        return response.data;
    } catch (error) {
        console.error('Error fetching movie actors:', error);
        throw error;
    }
};