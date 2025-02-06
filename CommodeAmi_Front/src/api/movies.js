import axios from 'axios'
import { fetchMovie } from '../api/movieDetails'; 

export const fetchBoxOfficeMovies = async() => {
    try {
        const response = await axios.get('/api/movie');
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

export const fetchHighRatedMovies = async () => {
  try {
    const response = await axios.get("/api/average-scope");

    console.log("🎬 평균 별점 API 응답:", response.data);

    // 응답이 배열인지 확인 후 변환
    const movies = Array.isArray(response.data) ? response.data : response.data.data;

    if (!Array.isArray(movies)) {
      console.error("예상과 다른 응답 형식:", response.data);
      throw new Error("Invalid API response format");
    }

    const topRatedMovies = movies.sort((a, b) => b.average_scope - a.average_scope).slice(0, 10);

    const enrichedMovies = await Promise.all(
      topRatedMovies.map(async (movie, index) => {
        try {
          const movieDetails = await fetchMovie(movie.movie_id); // 별 영화 정보 요청

          console.log(`영화 상세 정보 (ID: ${movie.movie_id})`, movieDetails); 
          return {
            movieId: movie.movie_id,
            title: movieDetails.data.title,
            posterUrl: movieDetails.data.poster_url, 
            averageScope: movie.average_scope, 
            high_rating_rank: index + 1, 
          };
        } catch (error) {
          console.warn(`영화 정보 가져오기 실패 (ID: ${movie.movie_id})`, error);
          return null;
        }
      })
    );
    console.log("변환된 최종 영화 리스트 (포스터 & 제목 포함):", enrichedMovies.filter(Boolean));

    return enrichedMovies.filter(Boolean); // null값 제거 
  } catch (error) {
    console.error("평균 별점이 높은 영화 데이터를 가져오는 중 오류 발생:", error);
    throw error;
  }
};

export const fetchWeatherRecommendedMovies = async () => {
  try {
    return new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(
        async (position) => {
          const lat = position.coords.latitude;
          const lon = position.coords.longitude;
          console.log(`현재 위치: 위도 ${lat}, 경도 ${lon}`);

          const response = await axios.get(`/api/recommendation`, {
            params: { lat, lon },
          });

          console.log("날씨 추천 영화 API 응답:", response.data);

          const movies = Array.isArray(response.data) ? response.data : response.data.data;
          resolve(movies.slice(0, 10));
        },
        async (error) => {
          console.warn("위치 정보를 가져올 수 없습니다. 기본값(서울) 사용", error);

          // 기본 위치 (서울)
          const defaultLat = 37.5665;
          const defaultLon = 126.9780;

          const response = await axios.get(`/api/recommendation`, {
            params: { lat: defaultLat, lon: defaultLon },
          });

          console.log("날씨 추천 영화 (기본 위치) API 응답:", response.data);

          const movies = Array.isArray(response.data) ? response.data : response.data.data;
          resolve(movies.slice(0, 10));
        }
      );
    });
  } catch (error) {
    console.error("날씨 기반 추천 영화를 가져오는 중 오류 발생:", error);
    throw error;
  }
};