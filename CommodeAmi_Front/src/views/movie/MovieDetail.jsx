import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchMovie } from '../../api/movieDetails';
import { Button } from 'primereact/button';
import './MovieDetail.css'
// import StarRating from './StarRating';
import StarDisplay from '../../components/StarDisplay';


const MovieDetail = () => {
    const { movieId } = useParams();
    const [movie, setMovie] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [selectedRating, setSelectedRating] = useState(0);
    const baseURL = 'https://image.tmdb.org/t/p/original';
    const formatDate = (timestamp) => {
        const date = new Date(timestamp);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // 0부터 시작하므로 +1
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`; // "2024-10-20"
    };


    useEffect(() => {
        const getMovieData = async () => {
            if (!movieId) {
                setError('Invalid movie ID');
                setLoading(false);
                return;
            }

            try {
                const movieData = await fetchMovie(movieId);
                const response = movieData.data

                // `stills`가 문자열이면 배열로 변환
                if (typeof response.stills === 'string') {
                    try {
                        response.stills = JSON.parse(response.stills);
                    } catch (err) {
                        console.error('Failed to parse stills:', err);
                        response.stills = []; // 변환 실패 시 빈 배열로 설정
                    }
                }

                setMovie(response);
                console.log("선택된 영화정보", response);
                console.log("movie 찍어보기", movie);
                console.log("선택된 영화의 첫 번째 스틸 컷", response.stills[1]);
            } catch (err) {
                console.error('Failed to fetch movie data:', err);
                setError('Failed to fetch movie data');
            }
        };

        getMovieData();
    }, [movieId]);

    // if (loading) return <p>Loading...</p>;
    // if (error) return <p>{error}</p>;

    // if (loading) return <p>Loading...</p>;
    // if (error) return <p>{error}</p>;

    if (!movie || !movie.stills || movie.stills.length === 0) {
        return <p>No movie or stills data available.</p>;
    }
    const formattedDate = formatDate(movie.released_at); // 날짜 변환
    const formatAudience = (movie.cumulative_audience / 10000).toFixed(1) + '만 명';

    return (
        <div className="movie-detail">
            <section className="movie-header">
                <img src={`${baseURL}${movie.stills[0]}`} alt={movie.title} />
                <div className="movie-header-overlay">    
                    <h1>{movie.title}</h1>
                    <p>{movie.original_title}</p>
                    <p>{movie.genre}</p>
                    <p>{movie.running_time}분 / {formattedDate} 개봉</p>
                    {movie.cumulative_audience ? (
                        <p>누적 관객 {formatAudience}</p>
                    ) : null}
                    
                </div> 
            </section>
            <section className="movie-details">
                <div className="movie-details-content">
                    <img src={movie.poster_url} alt={movie.title} />
                    <div className="movie-info">
                        <div className="movie-buttons">
                            <div className="star-display-container">
                                <StarDisplay rating={selectedRating} />
                            </div>
                            <Button 

                            />
                            <Button 

                            />
                            <Button 

                            />
                        </div>
                        <div className="movie-overview">
                            <h2>줄거리</h2>
                            <p>{movie.plot}</p>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
};

export default MovieDetail;
