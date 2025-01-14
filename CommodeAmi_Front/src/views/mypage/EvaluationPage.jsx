import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import StarDisplay from '../../components/StarDisplay';
import { fetchUserScopes } from '../../store/slices/scopeSlice';
import axios from 'axios';
import './EvaluationPage.css';

const EvaluationPage = () => {
    const [movieDetails, setMovieDetails] = useState([]); // 영화 상세 정보 저장
    const [sortOrder, setSortOrder] = useState('desc'); // 정렬 순서
    const [loading, setLoading] = useState(false); // 로딩 상태
    const navigate = useNavigate();
    const dispatch = useDispatch();

    // Redux에서 별점 데이터 가져오기
    const { scope } = useSelector((state) => state.scope);
    const userId = useSelector((state) => state.auth.user.id); // 사용자 ID 가져오기

    // `scope` 데이터를 영화 목록 형태로 변환
    // const movies = Object.entries(scope).map(([movieId, rating]) => ({
    //     movieId,
    //     rating,
    // }));
    const movies = Object.entries(scope).map(([_, rating]) => ({
        movieId: rating.movie_id, // movie_id를 movieId로 설정
        rating,
    }));

    useEffect(() => {
        // 사용자의 모든 별점 데이터 로드
        dispatch(fetchUserScopes(userId));
    }, [dispatch, userId]);

    // useEffect(() => {
    //     console.log('Redux에서 가져온 scope 데이터:', scope);
    
    //     // 별점이 있는 영화들의 `movie_id` 값을 추출
    //     const movieIds = Object.values(scope).map((item) => item.movie_id); // movie_id 값만 추출
    //     console.log('Movie IDs being requested:', movieIds);
    
    //     if (movieIds.length > 0) {
    //         loadMovieDetails(movieIds);
    //     }
    // }, [scope]);

    useEffect(() => {
        if (Object.keys(scope).length > 0) {
            const movieIds = Object.values(scope)
                .map((item) => item.movie_id)
                .filter((id) => id !== undefined); // undefined 제거
            console.log('Valid Movie IDs being requested:', movieIds);
    
            if (movieIds.length > 0) {
                loadMovieDetails(movieIds);
            }
        }
    }, [scope]);

    const loadMovieDetails = async (movieIds) => {
        setLoading(true); // 로딩 시작
    
        try {
            const token = localStorage.getItem('accessToken');
            if (!token) {
                console.warn('Access token is missing.');
                return;
            }
    
            // movieId가 undefined가 아닌 경우만 요청
            const validMovieIds = movieIds.filter((id) => id !== undefined);
            if (validMovieIds.length === 0) {
                console.warn('No valid movie IDs to request.');
                return;
            }
    
            const requests = validMovieIds.map((movieId) =>
                axios.get(`/api/movie/${movieId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                })
            );
    
            const responses = await Promise.allSettled(requests);
            const loadedDetails = responses
                .filter((res) => res.status === 'fulfilled')
                .map((res) => res.value.data);
    
            setMovieDetails((prev) => {
                const newDetails = loadedDetails.filter(
                    (detail) =>
                        !prev.some(
                            (item) => item.data.movie_id === detail.data.movie_id
                        )
                );
                return [...prev, ...newDetails];
            });
        } catch (error) {
            console.error('Failed to load movie details:', error.message || error);
        } finally {
            setLoading(false); // 로딩 종료
        }
    };

    const handleSortClick = () => {
        setSortOrder((prev) => (prev === 'desc' ? 'asc' : 'desc'));
    };

    const handleMovieClick = (movieId) => {
        navigate(`/movie/${movieId}`);
    };

    // 별점 순서에 따라 정렬된 영화 목록
    const sortedMovies = movies
    .filter((movie) => movie.movieId !== undefined) // movieId가 유효한 값인지 확인
    .sort((a, b) => {
        const ratingA = parseFloat(a.rating.scope);
        const ratingB = parseFloat(b.rating.scope);
        return sortOrder === 'desc' ? ratingB - ratingA : ratingA - ratingB;
    });

    return (
        <div className="evaluation-page">
            <div className="profile-sort-buttons">
                <button onClick={handleSortClick}>
                    별점순 {sortOrder === 'desc' ? '▼' : '▲'}
                </button>
            </div>
            <div className="movies-list">
                {loading && <p>로딩 중...</p>}
                {!loading && sortedMovies.length === 0 && <p>평가된 영화가 없습니다.</p>}
                {sortedMovies.map((movie, index) => {
                    const details = Array.isArray(movieDetails)
                    ? movieDetails.find((detail) => detail.data.movie_id === Number(movie.movieId)) // movieId를 숫자로 변환
                    : undefined;
                    console.log("sortedMovies 확인: ", sortedMovies);
                    console.log("movieDetails 확인: ", movieDetails);
                    console.log("details 확인: ", details);
                    return (
                        <div
                            // key={movie.movieId}
                            key={`${movie.movieId}-${index}`} // 고유한 key 값 설정
                            className="movie-item"
                            onClick={() => handleMovieClick(movie.movieId)}
                            style={{ cursor: 'pointer' }}
                        >
                            {details ? (
                                <>
                                    <img src={details.data.poster_url} alt={details.data.title} />
                                    <h3>{details.data.title}</h3>
                                    <StarDisplay
                                        rating={parseFloat(movie.rating.scope)}
                                        className="small-star"
                                    />
                                </>
                            ) : (
                                <h3>정보 없음</h3>
                            )}
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default EvaluationPage;
