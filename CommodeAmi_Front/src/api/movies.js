import axios from 'axios'

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