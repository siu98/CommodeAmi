import { useState, useEffect, React } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './hooks/useAuth';
import Navigation from './components/Navigation';
import LoginPage from './views/user/LoginPage';
import SignupPage from './views/user/SignupPage';
import SearchResults from './views/search/SearchResult';
import MovieSection from './views/movie/MovieSection';
import MovieDetail from './views/movie/MovieDetail';
import MyPage from './views/mypage/MyPage';
import CustomTicket from './views/customticket/CustomTicket';

import { fetchBoxOfficeMovies, fetchWeatherRecommendedMovies } from './api/movies';

import './App.css';

function App() {
  const { isLoggedIn, logout } = useAuth();
  const [boxOfficeMovies, setBoxOfficeMovies] = useState([]);
  const [recommendedMovies, setRecommendedMovies] = useState([]);

  useEffect(() => {
    const getMovies = async () => {
      try {
        // ✅ 박스오피스 영화 가져오기
        const movies = await fetchBoxOfficeMovies();
        setBoxOfficeMovies(movies);

        // ✅ 날씨 기반 추천 영화 가져오기
        const weatherMovies = await fetchWeatherRecommendedMovies();
        setRecommendedMovies(weatherMovies);
      } catch (error) {
        console.error('Failed to fetch movies:', error);
        setBoxOfficeMovies([]);
        setRecommendedMovies([]);
      }
    };

    getMovies();
  }, []);

  return (
    <Router>
      <Navigation isLoggedIn={isLoggedIn} handleLogout={logout} />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />

        <Route
          path="/"
          element={
            isLoggedIn ? (
              <Navigate to="/dashboard" />
            ) : (
              <MovieSection 
                title="박스오피스 순위" 
                movies={boxOfficeMovies} 
                scrollable 
                sectionId="box-office"
              />
            )
          }
        />

        {/* ✅ /dashboard에서 박스오피스 + 날씨 기반 추천 영화 같이 표시 */}
        <Route
          path="/dashboard"
          element={
            isLoggedIn ? (
              <>
                {/* 박스오피스 영화 섹션 */}
                <MovieSection 
                  title="박스오피스 순위" 
                  movies={boxOfficeMovies} 
                  scrollable 
                  sectionId="dashboard-box-office"
                />

                {/* ✅ 추가된 날씨 기반 추천 영화 섹션 */}
                <MovieSection 
                  title="날씨 기반 랜덤 추천 ☀️ 🌤️ ☔️" 
                  movies={recommendedMovies} 
                  scrollable
                  sectionId="recommendations"
                />
              </>
            ) : (
              <Navigate to="/" />
            )
          }
        />

        <Route
          path="/mypage"
          element={isLoggedIn ? <MyPage /> : <Navigate to="/" />}
        />

        <Route
          path="/search"
          element={isLoggedIn ? <SearchResults /> : <Navigate to="/" />}
        />

        <Route
          path="/mypage/customtickets"
          element={isLoggedIn ? <CustomTicket /> : <Navigate to="/" />}
        />

        <Route
          path="/movie/:movieId"
          element={<MovieDetail isLoggedIn={isLoggedIn} handleLogout={logout} />}
        />
      </Routes>
    </Router>
  );
}

export default App;
