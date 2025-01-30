import React from 'react';
import { Link } from 'react-router-dom';
import { Carousel } from 'primereact/carousel';
import './MovieSection.css';

const MovieSection = ({ title, movies }) => {
    const movieTemplate = (movie) => {
        return (
            <div className="movie-card-wrapper">
                <div className="movie-card">
                    <Link to={`/movie/${movie.movieId}/`}>
                        <img src={movie.posterUrl} alt={movie.title} className="movie-poster" />
                    </Link>
                    <div className="movie-info">
                        <h4>{movie.title}</h4>
                        {movie.boxOfficeRank && (
                            <p className="rank-label">{movie.boxOfficeRank}</p>
                        )}
                    </div>
                </div>
            </div>
        );
    };

    return (
        <section className="movie-section">
            <h2>{title}</h2>
            <Carousel
                value={movies}
                itemTemplate={movieTemplate}
                numVisible={5} /* 기본적으로 5개 보이도록 설정 */
                numScroll={1} /* 한 번에 스크롤될 아이템 개수 */
                autoplayInterval={5000} /* 5초 간격으로 자동 이동 */
                responsiveOptions={[
                    { breakpoint: '1200px', numVisible: 5, numScroll: 1 }, /* 큰 화면에서도 5개 */
                    { breakpoint: '1024px', numVisible: 4, numScroll: 1 }, /* 중간 화면에서 4개 */
                    { breakpoint: '768px', numVisible: 3, numScroll: 1 }, /* 작은 화면에서 3개 */
                    { breakpoint: '560px', numVisible: 2, numScroll: 1 }, /* 모바일 화면에서 2개 */
                ]}
            />
        </section>
    );
};

export default MovieSection;
