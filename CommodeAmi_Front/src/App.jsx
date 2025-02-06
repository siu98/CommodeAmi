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

import { fetchBoxOfficeMovies, fetchWeatherRecommendedMovies, fetchHighRatedMovies } from './api/movies';

// import './App.css';

function App() {
  const { isLoggedIn, logout } = useAuth();
  const [boxOfficeMovies, setBoxOfficeMovies] = useState([]);
  const [recommendedMovies, setRecommendedMovies] = useState([]);
  const [highRatedMovies, setHighRatedMovies] = useState([]); 
  useEffect(() => {
    const getMovies = async () => {
      try {

        const movies = await fetchBoxOfficeMovies();
        setBoxOfficeMovies(movies);

        const weatherMovies = await fetchWeatherRecommendedMovies();
        setRecommendedMovies(weatherMovies);

        const highRated = await fetchHighRatedMovies();
        setHighRatedMovies(highRated);
      } catch (error) {
        console.error('Failed to fetch movies:', error);
        setBoxOfficeMovies([]);
        setRecommendedMovies([]);
        setHighRatedMovies([]);
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

        <Route
          path="/dashboard"
          element={
            isLoggedIn ? (
              <>
                <MovieSection 
                  title="박스오피스 순위" 
                  movies={boxOfficeMovies} 
                  scrollable 
                  sectionId="dashboard-box-office"
                />

                <MovieSection 
                  title="평균 별점 높은 영화" 
                  movies={highRatedMovies} 
                  scrollable
                  sectionId="high-rated-movies"
                />

                <MovieSection 
                  title="날씨 기반 랜덤 추천 ☀️ 🌤️ ☔️ 🌨️" 
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
