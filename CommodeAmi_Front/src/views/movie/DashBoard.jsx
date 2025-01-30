import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { fetchBoxOfficeMovies } from './api/movies';
// import { scrollLeft, scrollRight, handleScroll, updateScrollButtons } from './utils/scroll';
import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import './App.css';
import MovieSection from './MovieSection';
import FilterSection from './FilterSection';
import MovieDetail from './MovieDetail';
// import SearchResults from './SearchResults';
// import MyPage from './MyPage';q

function DashBoard({ handleLogout }) {
  const [boxOfficeMovies, setBoxOfficeMovies] = useState([]);
  const [highRatedMovies, setHighRatedMovies] = useState([]);
  const [recommendedMovies, setRecommendedMovies] = useState([]); // 추가
  const [birthYearOptions, setBirthYearOptions] = useState([]);
  const [selectedGender, setSelectedGender] = useState(null);
  const [selectedBirthYear, setSelectedBirthYear] = useState(null);
  const [filterVisible, setFilterVisible] = useState(false);
  const [filteredMovies, setFilteredMovies] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const loadMovies = async () => {
      try {
        const movies = await fetchBoxOfficeMovies(); // API 호출
        setBoxOfficeMovies(movies); // API 응답 데이터 설정
        
        // const highRatedData = await fetchHighRatedMovies();
        // setHighRatedMovies(highRatedData);

        // const weatherRecommendedData = await fetchWeatherRecommendedMovies();
        // setRecommendedMovies(weatherRecommendedData);

      } catch (error) {
        setBoxOfficeMovies([]);
        // setHighRatedMovies([]);
        // setRecommendedMovies([]);
        console.error('Error loading movies:', error);
      }
    };

    loadMovies();
  }, []);

  useEffect(() => {
    const currentYear = new Date().getFullYear();
    const startYear = 1900;
    const years = [];
    for (let year = currentYear; year >= startYear; year--) {
      years.push(year);
    }
    setBirthYearOptions(years);
  }, []);

  useEffect(() => {
    updateScrollButtons('box-office');
    updateScrollButtons('high-rated');
    updateScrollButtons('recommendations');
  }, [boxOfficeMovies, highRatedMovies, recommendedMovies]);

  const toggleFilter = () => {
    setFilterVisible(!filterVisible);
  };

  const applyFilter = () => {
    const filtered = recommendedMovies.filter(movie => {
      if (selectedGender && selectedBirthYear) {
        return true;
      }
      return true;
    });
    setFilteredMovies(filtered);
    console.log(`Selected Gender: ${selectedGender}, Birth Year: ${selectedBirthYear}`);
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    const trimmedQuery = searchQuery.trim();
    if (trimmedQuery) {
      axios.get(`http://127.0.0.1:8000/api/movies/?search=${trimmedQuery}`)
        .then(response => {
          setSearchResults(response.data);
          navigate(`/dashboard/search?query=${trimmedQuery}`);
        })
        .catch(error => {
          console.error('Error fetching search results:', error);
        });
    } else {
      alert('검색어를 입력하세요.');
    }
  };

  return (
    <div className="dashboard">
      <Routes>
        <Route path="/" element={
          <>
            <MovieSection 
              title="박스오피스 순위" 
              movies={boxOfficeMovies} 
              scrollable 
              sectionId="box-office"
              scrollLeft={scrollLeft}
              scrollRight={scrollRight}
              handleScroll={handleScroll}
              onMovieClick={(id) => navigate(`/dashboard/movies/${id}`)}
              isLoggedIn={true}
            />
            <MovieSection 
              title="평균 별점이 높은 영화" 
              movies={highRatedMovies} 
              scrollable
              sectionId="high-rated"
              scrollLeft={scrollLeft}
              scrollRight={scrollRight}
              handleScroll={handleScroll}
              onMovieClick={(id) => navigate(`/dashboard/movies/${id}`)}
              isLoggedIn={true}
            /> 

            {/* 추가된 날씨 기반 추천 섹션 */}
            <MovieSection 
              title="날씨 기반 랜덤 추천 ☀️ 🌤️ ☔️" 
              movies={recommendedMovies} 
              scrollable
              sectionId="recommendations"
              scrollLeft={scrollLeft}
              scrollRight={scrollRight}
              handleScroll={handleScroll}
              onMovieClick={(id) => navigate(`/dashboard/movies/${id}`)}
              isLoggedIn={true}
            />
          </>
        } />
        <Route path="/movie/:id" element={<MovieDetail />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/search" element={<SearchResults searchQuery={searchQuery} searchResults={searchResults} />} />
      </Routes>
    </div>
  );
}

export default DashBoard;

