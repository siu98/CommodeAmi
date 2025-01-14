import React, { useEffect, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { searchMovies as fetchSearchMovies } from '../../api/searchMovies';
import './SearchResults.css'; 

function SearchResult() { 
  const [searchParams] = useSearchParams();
  const [results, setResults] = useState([]);
  const query = searchParams.get('query'); 
  const isLoggedIn = useSelector((state) => state.auth.isAuthenticated);
  const navigate = useNavigate();

  useEffect(() => {
    console.log('isLoggedIn 상태:', isLoggedIn); 
    const fetchMovies = async () => {
      if (!isLoggedIn) { 
        alert('로그인이 필요합니다.');
        return;
      }

      if (!query) {
        setResults([]); // 쿼리가 없으면 빈 배열로 설정
        setErrorMessage("검색어를 입력하세요.");
        return;
      }

    //   if (query) {
    //     try {
    //       const data = await fetchSearchMovies(query);
    //       setResults(data);
    //     } catch (error) {
    //       console.error('Error fetching search results:', error);
    //     }
    //   }
    // };

    try {
      const data = await fetchSearchMovies(query);
      if (data && data.length > 0) {
        setResults(data); // 검색 결과 설정
        // setErrorMessage(""); // 에러 메시지 초기화
      } else {
        setResults([]); // 결과가 없으면 빈 배열로 설정
        // setErrorMessage("검색 결과가 없습니다."); // 에러 메시지 설정
      }
    } catch (error) {
      console.error("검색 오류:", error);
      setResults([]);
      // setErrorMessage("영화 검색 중 오류가 발생했습니다.");
    }
  };

    fetchMovies();
  }, [query, isLoggedIn]); 

return (
  <div className="search-results">
    <h2>검색 결과: "{query}"</h2>
    {results.length === 0 ? (
      <p>검색 결과가 없습니다.</p>
    ) : (
      <div className="movie-grid">
        {results.map((movie) => (
          <div className="movie-card" key={movie.movie_id} onClick={() => navigate(`/movie/${movie.movie_id}`)}>
            <img src={movie.poster_url} alt={movie.title} />
            <p>{movie.title} ({new Date(movie.released_at).getFullYear()})</p>
          </div>
        ))}
      </div>
    )}
  </div>
);
}
export default SearchResult;
