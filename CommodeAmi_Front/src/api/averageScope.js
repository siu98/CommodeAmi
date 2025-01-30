import axios from 'axios';

export const fetchAverageScope = async(movieId) => {
    try {
        console.log("평균 별점 movieId 확인: ", movieId);
        const response = await axios.get(`/api/average-scope/${movieId}`);
        const averageScopeData = response.data;
        // if (!averageScopeData) {
        //     return;
        // }
        console.log("fetchAverageScope API 응답확인: ", averageScopeData);
        return averageScopeData;
    } catch(error) {
        console.error("평균 별점을 가져오는 중 오류가 발생하였습니다.", error);
        throw error;
    }
}