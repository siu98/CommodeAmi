import axios from 'axios'
import { useSelector } from "react-redux";
export const fetchBoxOfficeMovies = async() => {
    try {
        const response = await axios.get('/api/movie');

        // 응답 데이터 확인
        console.log('API 응답:', response.data);

        // 배열 형태가 아닐 경우 적절히 처리
        const movies = Array.isArray(response.data) ? response.data : response.data.data;


        const filteredAndSortedMovies = movies
          .filter((movie) => movie.boxOfficeRank >= 1 && movie.boxOfficeRank <= 10)
          .sort((a, b) => a.boxOfficeRank - b.boxOfficeRank);
        return filteredAndSortedMovies;
        
      } catch (error) {
        console.error('박스오피스 영화 데이터를 가져오는 중 오류 발생:', error);
        throw error;
      }
}
export const fetchWeatherRecommendedMovies = async () => {
  try {
    return new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(
        async (position) => {
          const lat = position.coords.latitude;
          const lon = position.coords.longitude;
          console.log(`🌍 현재 위치: 위도 ${lat}, 경도 ${lon}`);

          const response = await axios.get(`/api/recommendation`, {
            params: { lat, lon },
          });

          console.log("🎬 날씨 추천 영화 API 응답:", response.data);

          // ✅ 응답이 배열인지 확인 후 변환
          const movies = Array.isArray(response.data) ? response.data : response.data.data;
          resolve(movies.slice(0, 10));
        },
        async (error) => {
          console.warn("🚨 위치 정보를 가져올 수 없습니다. 기본값(서울) 사용", error);

          // ✅ 기본 위치 (서울) 사용
          const defaultLat = 37.5665;
          const defaultLon = 126.9780;

          const response = await axios.get(`/api/recommendation`, {
            params: { lat: defaultLat, lon: defaultLon },
          });

          console.log("🎬 날씨 추천 영화 (기본 위치) API 응답:", response.data);

          // ✅ 응답이 배열인지 확인 후 변환
          const movies = Array.isArray(response.data) ? response.data : response.data.data;
          resolve(movies.slice(0, 10));
        }
      );
    });
  } catch (error) {
    console.error("❌ 날씨 기반 추천 영화를 가져오는 중 오류 발생:", error);
    throw error;
  }
};