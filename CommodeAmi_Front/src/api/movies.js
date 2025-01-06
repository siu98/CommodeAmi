import axios from 'axios'

export const fetchBoxOfficeMovies = async() => {
    try {
        const response = await axios.get('http://localhost:8000/api/movies/');
        const filteredAndSortedMovies = response.data
          .filter((movie) => movie.box_office_rank >= 1 && movie.box_office_rank <= 10)
          .sort((a, b) => a.box_office_rank - b.box_office_rank);
        return filteredAndSortedMovies;
        
      } catch (error) {
        console.error('박스오피스 영화 데이터를 가져오는 중 오류 발생:', error);
        throw error;
      }
}