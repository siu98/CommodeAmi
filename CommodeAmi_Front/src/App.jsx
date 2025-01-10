import { useState, useEffect, React } from 'react'
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import Navigation from './components/Navigation';
import Counter from './features/Counter';
import { useAuth } from './hooks/useAuth';
import LoginPage from './views/user/LoginPage';
import SignupPage from './views/user/SignupPage';
import MovieSection from './views/movie/MovieSection';
import MovieDetail from './views/movie/MovieDetail';
import { fetchBoxOfficeMovies } from './api/movies';

import './App.css'

function App() {
  const { isLoggedIn, logout } = useAuth();
  const [boxOfficeMovies, setBoxOfficeMovies] = useState([]);

  useEffect(() => {
    const getMovies = async () => {
      try {
        const movies = await fetchBoxOfficeMovies();
        setBoxOfficeMovies(movies);
      } catch (error) {
        console.error('Failed to fetch movies:', error);
      }
    };

    getMovies();
  }, []);
  
  return (
      <Router>
        <Navigation isLoggedIn={isLoggedIn} handleLogout={logout} />
        {/* <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/" element={
            <MovieSection 
              title="박스오피스 순위" 
              movies={boxOfficeMovies} 
              scrollable 
              sectionId="box-office"
            />
          } />
  
          <Route path="/movie/:movieId" element={<MovieDetail isLoggedIn={isLoggedIn} handleLogout={logout} />} />
        </Routes> */}
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
              <MovieSection 
                title="박스오피스 순위 (로그인 전용)" 
                movies={boxOfficeMovies} 
                scrollable 
                sectionId="dashboard-box-office"
              />
              ) : (
              <Navigate to="/login" />
              )
            }
          />
          <Route path="/movie/:movieId" element={<MovieDetail isLoggedIn={isLoggedIn} handleLogout={logout} />} />
        </Routes>
      </Router>
  );
}

export default App
