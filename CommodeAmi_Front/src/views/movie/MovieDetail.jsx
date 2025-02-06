import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { fetchMovie, fetchActors } from '../../api/movieDetails';
import { fetchMovieScope, setLoading, setError, setScope } from '../../store/slices/scopeSlice';
import { fetchMovieReview, setReview } from '../../store/slices/reviewSlice';
import { fetchReviewByMovieId } from '../../api/reviews';
import { fetchAverageScope } from '../../api/averageScope'; 
import { Button } from 'primereact/button';
import { Image } from 'primereact/image';
import { Dialog } from 'primereact/dialog';
import { Carousel } from 'primereact/carousel';
import { InputTextarea } from 'primereact/inputtextarea';
import './MovieDetail.css';
import StarDisplay from '../../components/StarDisplay';
import StarRating from '../../components/StarRating';
import axios from 'axios';

const MovieDetail = () => {
    const { movieId } = useParams();
    const [movie, setMovie] = useState([]);
    const [actors, setActors] = useState([]);
    const [reviews, setReviews] = useState([]);
    const [movieReviews, setMovieReviews] = useState([]);
    const [selectedRating, setSelectedRating] = useState(0);
    const [localReview, setLocalReview] = useState('');
    const [averageScope, setAverageScope] = useState(null); 
    const [numberOfPeople, setNumberOfPeople] = useState(null); 
    const [showAllActorsPopup, setShowAllActorsPopup] = useState(false);
    const [dialogVisible, setDialogVisible] = useState(null); 
    const [viewingDate, setViewingDate] = useState(''); 

    const [movieActors, setMovieActors] = useState([]);
    const baseURL = 'https://image.tmdb.org/t/p/original';

    const dispatch = useDispatch();
    const { accessToken, user } = useSelector((state) => state.auth);
    const { scope, loading, error } = useSelector((state) => state.scope);
    const { review } = useSelector((state) => state.review);
    const userId = user?.userId; // user 객체에서 userId 추출
    const movieRating = scope?.[movieId] || 0;
    const movieReview = review?.[movieId] || { review: '' }; // 기본값 설정

    const parseYouTubeUrls = (youtubeData) => {
        try {
            let urls = JSON.parse(youtubeData); // JSON 문자열을 배열로 변환
            return urls
                .filter(url => url.includes("youtube.com/watch?v=")) // 광고 URL 제거
                .map(url => url.replace("watch?v=", "embed/"));
        } catch (error) {
            console.error("YouTube URL JSON 파싱 오류:", error);
            return [];
        }
    };

    const formatDate = (timestamp) => {
        const date = new Date(timestamp);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
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
                const response = movieData.data;
    
                if (typeof response.stills === 'string') {
                    try {
                        response.stills = JSON.parse(response.stills);
                    } catch (err) {
                        console.error('Failed to parse stills:', err);
                        response.stills = [];
                    }
                }
                if (typeof response.trailers === 'string') {
                    try {
                        response.trailers = JSON.parse(response.trailers);
                    } catch (err) {
                        console.error('Failed to parse trailers:', err);
                        response.trailers = [];
                    }
                }

                // 트레일러 URL에서 key 값을 추출하여 YouTube embed URL로 변환
                if (Array.isArray(response.trailers)) {
                    response.trailers = response.trailers.map(trailer => {
                    const key = extractYouTubeKey(trailer);
                    return `https://www.youtube.com/embed/${key}?autoplay=0`;
                    });
                }
    
                setMovie(response);
                // console.log("setMovie 확인: ", setMovie(response));
            } catch (err) {
                console.error('Failed to fetch movie data:', err);
                setError('Failed to fetch movie data');
            } finally {
                setLoading(false);
            }
        };
    
        getMovieData();
    }, [movieId]);


    // 배우 데이터를 가져오는 추가 작업
    useEffect(() => {
        if (!movieId) return; 
        const getActorData = async () => {
            try {
                const actorData = await fetchActors(movieId);
                const response = actorData.data;
                console.log("actors 확인: ", actorData);
                setActors(response);
            } catch (err) {
                console.error('Failed to fetch actor data:', err);
                setError('Failed to fetch actor data');
            }
        };
    
        getActorData(); 
    
    }, [movieId]);

    useEffect(() => {
        if (accessToken && userId && movieId) {
            console.log('Fetching movie scope with access token.');
            dispatch(fetchMovieScope(userId, movieId));
        } else {
            console.warn('User is not logged in or required data is missing.');
        }
    }, [accessToken, userId, movieId, dispatch]);

    useEffect(() => {
        if (accessToken && userId && movieId) {
            dispatch(fetchMovieReview(userId, movieId));
        }
    }, [accessToken, userId, movieId, dispatch]);
    
    useEffect(() => {
        if (movieReview?.review) {
            setLocalReview(movieReview.review);
        }
    }, [movieReview]); // Redux 상태가 변경될 때 로컬 상태를 업데이트

    useEffect(() => {
        const getReviews = async () => {
            if (!movieId || !accessToken) return;

            try {
                const reviews = await fetchReviewByMovieId(movieId, accessToken);
                const reviewsArray = Array.isArray(reviews) ? reviews : Object.values(reviews); // 배열로 변환
                console.log("리뷰 데이터 배열 형태:", reviewsArray);
            setMovieReviews(reviewsArray); // 상태 업데이트
            } catch (err) {
                console.error("리뷰를 가져오는 중 오류 발생:", err);
                setError("리뷰를 가져오지 못했습니다.");
            } 
        };

        getReviews();
    }, [movieId, accessToken]);

    useEffect(() => {
        const getAverageScope = async () => {
            try {
                const response = await fetchAverageScope(movieId);
                console.log('fetchAverageScope API 응답확인:', response);
                
                if (response.success && response.data) {
                    setAverageScope(response.data.average_scope); 
                    setNumberOfPeople(response.data.number_of_people); 
                } else {
                    console.warn('API 응답이 예상과 다릅니다:', response);
                }
            } catch (err) {
                console.error('Failed to fetch average scope:', err);
            }
        };
    
        if (movieId) {
            getAverageScope();
        }
    }, [movieId]);
    

    if (loading) {
        return <p>로딩 중...</p>;
    }
    
    if (error) {
        return <p>오류가 발생했습니다: {error}</p>;
    }

    if (!movie || !movie.stills || movie.stills.length === 0) {
        return <p>영화 정보를 불러오는 중입니다.</p>;
        
    }


    const formattedDate = formatDate(movie.released_at);
    const formatAudience = (movie.cumulative_audience / 10000).toFixed(1) + '만 명';

    const handleShowAllActors = () => {
        setShowAllActorsPopup(true);
    };

    const handleClosePopup = () => {
        setShowAllActorsPopup(false); // 팝업 닫기
    };

    const handleSelectRating = (scope) => {
        setSelectedRating(scope);
      };


      const handleSaveRating = async (movieId, ratingValue) => {
        try {
            // 1. 토큰 확인
            if (!accessToken) {
                alert('로그인이 필요합니다.');
                throw new Error('No access token found. Please log in.');
            }
    
            // 2. 요청 데이터 준비
            const requestData = { scope: ratingValue };
    
            // 3. Axios POST 요청
            const response = await axios.post(
                `/api/scope/${movieId}/${userId}`,
                requestData,
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                        'Content-Type': 'application/json',
                    },
                }
            );
    
            // 4. 서버 요청 성공 시 Redux 상태 업데이트
            if (response.status === 200 || response.status === 201) {
                const { scope, id: scopeId } = response.data.data; 
                // Redux 상태 업데이트
                dispatch(setScope({ movieId, scope, scopeId })); 

                // const message =
                //     response.status === 201
                //         ? '별점이 생성되었습니다.'
                //         : '별점이 생성되었습니다.';
                // alert(message);
                alert('별점이 저장되었습니다.');


                dispatch(fetchMovieScope(userId, movieId));
                fetchAverageScope(movieId).then(response => {
                    if (response.success && response.data) {
                        setAverageScope(response.data.average_scope); 
                        setNumberOfPeople(response.data.number_of_people); 
                    }
                });

                setDialogVisible(null);
            } else {
                alert('별점 저장에 실패했습니다.');
            }
        } catch (error) {
            // 5. 에러 처리
            console.error(
                'Error saving rating:',
                error.response ? error.response.data : error.message
            );
    
            if (error.response?.status === 401) {
                alert('인증 오류: 로그인 상태를 확인하세요.');
            } else {
                alert(
                    `별점 저장 중 오류가 발생했습니다: ${
                        error.response ? JSON.stringify(error.response.data) : error.message
                    }`
                );
            }
        }
    };

    const reviewData = {
        review: localReview,
        scopeId: movieRating.scopeId,
        movieId: Number(movieId),
        userId,
    }

    console.log("전송할 reviewData:", reviewData); 


    const handleSaveReview = async () => {
        if (!movieRating) {
            alert('별점을 먼저 선택해주세요.');
            return;
        }

        // 리뷰 작성 여부 확인
        if (!localReview.trim()) {
            alert('리뷰를 작성해주세요.');
            return;
        }

        // 3. 리뷰 작성 및 수정

        try {
            const response = await axios.post(`/api/review/${movieId}/${userId}`, reviewData, 
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                        'Content-Type': 'application/json',
                    },
                }
            );

            if (response.status === 200 || response.status === 201) {
                dispatch(setReview({ movieId, review: localReview}));
                alert('리뷰가 저장되었습니다.');

            // ✅ fetchMovieReview() 실행 후 결과를 받아 상태 업데이트
            const updatedReviews = await fetchReviewByMovieId(movieId, accessToken);
            setMovieReviews(updatedReviews);

            // ✅ 다이얼로그 닫기
            setDialogVisible(null);
                // setDialogVisible(null);
            } else {
                alert('리뷰 저장에 실패했습니다.');
            }
        } catch (error) {
            console.error('리뷰 저장 중 오류 발생:', error);
            alert('리뷰 저장 중 오류가 발생했습니다.');
        }
    }


    return (
        <div className="movie-detail">
            <main>
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

                                <StarDisplay rating={movieRating} />
                                {console.log("movieRating:", movieRating)}
                                <p>
                                    평균별점: {averageScope || 'N/A'}
                                    {numberOfPeople !== null && ` (${numberOfPeople}명)`}
                                </p>
                            </div>
                            <Button label="별점" onClick={() => setDialogVisible('scope')} />
                            <Button label="리뷰" onClick={() => setDialogVisible('review')} />
                            <Button label="관람일자" onClick={() => setDialogVisible('viewingDate')} />
                        </div>
                        <div className="movie-overview">
                            <h2>줄거리</h2>
                            <p>{movie.plot}</p>
                        </div>
                        <section className="movie-reviews">
                            {movieReviews && movieReviews.length > 0 ? (
                                <>
                                    <h2>리뷰 {movieReviews.length}</h2>
                                    <div className="review-list">
                                        {movieReviews
                                            .slice(0, 3) // 최대 3개의 리뷰만 표시
                                            .map((review) => (
                                            <div className="review-item" key={review.review_id}>
                                                <div className="review-header">
                                                    <span className="review-username">작성자: {review.nickname}</span>
                                                    <span className="review-rating">⭐{review.scope}</span>
                                                </div>
                                                <p>{review.review}</p>
                                            </div>
                                        ))}
                                    </div>
                                    {movieReviews.length > 3 && (
                                        <button className="more-button" onClick={handleMoreReviewsClick}>
                                            더보기
                                        </button>
                                    )}
                                </>
                                ) : (
                                <p>리뷰가 없습니다!</p>
                            )}
                        </section>
                    </div>
                </div>
            </section>

            {/* 별점 다이얼로그 */}
            <Dialog
                header="별점"
                visible={dialogVisible === 'scope'}
                onHide={() => setDialogVisible(null)}
                style={{ width: '50vw' }}
                >
                <div className="rating-popup" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    <div className="popup-content">
                        <StarRating onRatingSelect={handleSelectRating} initialRating={selectedRating} />
                        <div className="popup-buttons">
               
                            <Button
                                label="확인"
                                // onClick={handleSaveRating} // 별점 저장 함수 호출
                                onClick={() => handleSaveRating(movieId, selectedRating)} 
                            />
                    
                        </div>
                    </div>
                </div>
            </Dialog>

            {/* 리뷰 다이얼로그 */}
            <Dialog
                // header="리뷰 작성"
                header={movie.title}
                visible={dialogVisible === 'review'}
                onHide={() => setDialogVisible(null)}
                style={{ width: '50vw' }}
            >
                <div className="dialog-content">
                    {/* <p>리뷰를 입력해주세요:</p> */}
                    <InputTextarea
                        value={localReview}
                        onChange={(e) => setLocalReview(e.target.value)}
                        placeholder="리뷰 입력"
                        rows={5}
                        cols={50}
                        style={{ width: '100%', height: '150px' }} // 원하는 크기
                    />
                </div>
                <div className="review-button">
                    <Button label="확인" 
                        onClick={() => handleSaveReview(movieId, selectedRating)} 
                    />
                </div>
            </Dialog>

            {/* 관람일자 다이얼로그 */}
            <Dialog
                header="관람일자를 입력해주세요"
                visible={dialogVisible === 'viewingDate'}
                rows={5} cols={30}
                onHide={() => setDialogVisible(null)}
                style={{ width: '50vw' }}
                >
                <div className="dialog-content">
                    <InputTextarea
                        value={viewingDate}
                        onChange={(e) => setViewingDate(e.target.value)}
                        placeholder="YYYY-MM-DD"
                    />
                </div>
            </Dialog>
            
            <section className="movie-cast">
                {actors.length > 0 ? (
                <>
                    <h2>출연 {actors.length}</h2>
                    <div className="cast-list">
                        {actors.slice(0, 9).map(actor => (
                            <div className="cast-item" key={actor.actor_id}>
                                <img src={`${baseURL}${actor.profile_image}`} />

                                <p>{actor.name}</p>
                            </div>
                        ))}
                    </div>

                    {actors.length > 8 && (
                        <button label="더보기"  className="more-button" onClick={handleShowAllActors}>더보기</button>
                    )}

                    {/* Dialog 구현 */}
                    <Dialog
                        header="출연 배우 목록"
                        visible={showAllActorsPopup}
                        style={{ width: '50vw' }}
                        onHide={handleClosePopup}
                    >
                        <div className="cast-popup">
                            {actors.map(actor => (
                                <div className="cast-item" key={actor.actor_id}>
                                    <img
                                        src={`${baseURL}${actor.profile_image}`}
                                        alt={actor.name}
                                    />
                                    <p>{actor.name}</p>
                                </div>
                            ))}
                        </div>
                    </Dialog>

                </>
                ) : (
                <p>Loading actors...</p>
                )}
            </section>
            <section className="movie-stills">
                <h2>스틸컷{movie.stills.length}</h2>
                <div className="still-list">

                <Carousel
                    value={movie.stills} 
                    numVisible={4}
                    numScroll={4}
                    // circular 
                    autoplayInterval={10000}
                    itemTemplate={(still, index) => (
                    <div className="still-list" key={index}>
                        <Image
                            src={`${baseURL}${still}`}
                            alt={`Still ${index + 1}`}
                            preview
                            style={{ cursor: 'pointer', width: '100%' }}
                        />
                    </div>
                    )}
                />
            </div>
        </section>
        <section className="movie-trailers">
            <h2>트레일러</h2>
            {Array.isArray(movie.trailers) && movie.trailers.length > 0 ? (
                <Carousel
                    value={movie.trailers} 
                    numVisible={3} 
                    numScroll={3} 
                    autoplayInterval={10000} 
                    itemTemplate={(trailer, index) => (
                        <div className="trailer-item" key={index}>
                            <iframe
                                width="400" 
                                height="220"
                                src={trailer} 
                                title={`Movie Trailer ${index + 1}`}
                                frameBorder="0"
                                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                allowFullScreen
                            ></iframe>
                        </div>
                    )}
                />
            )  : (
            <p>트레일러가 없습니다.</p>
            )}
        </section>
        <section className="movie-review-videos">
            <h2>리뷰 영상</h2>
            {movie.youtube_url ? (
                (() => {
                    let youtubeVideos = [];

                    try {
                        youtubeVideos = JSON.parse(movie.youtube_url); 
                        youtubeVideos = youtubeVideos.map((url) => 
                            url.replace("watch?v=", "embed/")
                            );
                        } catch (error) {
                        console.error("YouTube URL JSON 파싱 오류:", error);
                    }

                    return Array.isArray(youtubeVideos) && youtubeVideos.length > 0 ? (
                    <Carousel
                        value={youtubeVideos} 
                        numVisible={3} 
                        numScroll={3} 
                        autoplayInterval={10000} // 자동 재생 간격 (10초)
                        itemTemplate={(video, index) => (
                            <div className="review-video-item" key={index}>
                                <iframe
                                    width="400" 
                                    height="220"
                                    src={video}
                                    title={`Review Video ${index + 1}`}
                                    frameBorder="0"
                                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                    allowFullScreen
                                ></iframe>
                            </div>
                        )}
                    />
                ) : (
                    <p>리뷰 영상이 없습니다.</p>
                );
            })()
            ) : (
            <p>리뷰 영상이 없습니다.</p>
            )}
        </section>
    </main>
    </div>

        
    );
};

export default MovieDetail;


function extractYouTubeKey(url) {
    const urlObj = new URL(url);
    return urlObj.searchParams.get('v');
  }