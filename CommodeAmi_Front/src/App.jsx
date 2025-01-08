import { useState, useEffect, React } from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
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
        <Routes>
          {/* <Route path="/"  /> */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/" element={
            <MovieSection 
              title="박스오피스 순위" 
              movies={boxOfficeMovies} 
              scrollable 
              sectionId="box-office"
              // scrollLeft={scrollLeft}
              // scrollRight={scrollRight}
              // handleScroll={handleScroll}
            />
          } />
          {/* <Route path="/dashboard" element={isLoggedIn ? <Dashboard /> : <Login />} /> */}
          {/* <Route path="/counter" element={<Counter />} /> */}
          <Route path="/movie/:movieId" element={<MovieDetail isLoggedIn={isLoggedIn} handleLogout={logout} />} />
        </Routes>
      </Router>
  );
}

export default App
