import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
// import { addRating } from '../../store/slices/authSlice';
import { fetchMovie, fetchActors } from '../../api/movieDetails';
import { fetchMovieScope, setLoading, setError, setScope } from '../../store/slices/scopeSlice';
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
    const [selectedRating, setSelectedRating] = useState(0);
    const [dialogVisible, setDialogVisible] = useState(null); // 다이얼로그 상태 관리
    const [review, setReview] = useState(''); // 리뷰 입력값
    const [viewingDate, setViewingDate] = useState(''); // 관람일자 입력값

    const [movieActors, setMovieActors] = useState([]);
    const baseURL = 'https://image.tmdb.org/t/p/original';

    const dispatch = useDispatch(); // useDispatch를 최상위에서 호출
    const { accessToken, user } = useSelector((state) => state.auth); // useSelector를 최상위에서 호출
    const { scope, loading, error } = useSelector((state) => state.scope);
    const userId = user?.userId; // user 객체에서 userId 추출
    const movieRating = scope?.[movieId] || 0;
    console.log("movieDetail에서 userId 호출: ", userId);

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
        if (!movieId) return; // movieId가 없으면 실행하지 않음
        const getActorData = async () => {
            try {
                const actorData = await fetchActors(movieId);
                const response = actorData.data;
                console.log("actors 확인: ", actorData);
                setActors(response); // 배우 정보를 상태에 저장
            } catch (err) {
                console.error('Failed to fetch actor data:', err);
                setError('Failed to fetch actor data');
            }
        };
    
        getActorData(); // 배우 데이터를 가져오는 함수 호출
    
    }, [movieId]);

    useEffect(() => {
        if (accessToken && userId && movieId) {
            console.log('Fetching movie scope with access token.');
            dispatch(fetchMovieScope(userId, movieId));
        } else {
            console.warn('User is not logged in or required data is missing.');
        }
    }, [accessToken, userId, movieId, dispatch]);
    
    

    // const fetchMovieRating = async (movieId) => {
    //     try {
    //         const response = await axios.get(`/api/scope/${userId}/${movieId}`, {
    //             headers: {
    //                 Authorization: `Bearer ${accessToken}`,
    //             },
    //         });
    
    //         if (response.status === 200) {
    //             console.log("별점 response확인: ", response.data);
    //             const ratingValue = response.data.scope; // 서버에서 별점 데이터 가져오기
                
    //             dispatch(addRating({ movieId, ratingValue })); // Redux 상태 업데이트
    //             console.log('Updated Redux Scope:', { movieId, scope: ratingValue });
    //         }
    //     } catch (error) {
    //         console.error('Failed to fetch movie rating:', error.message);
    //     }
    // };

    // useEffect(() => {
    //     const fetchRating = async () => {
    //         if (!movieId || !userId) {
    //             console.error('Missing movieId or userId:', { movieId, userId });
    //             return;
    //         }
    
    //         console.log('Fetching rating for movieId:', movieId);
    //         await fetchMovieRating(movieId);
    //     };
    
    //     fetchRating();
    // }, [movieId, userId]); // movieId 또는 userId가 변경될 때 호출

        // **로딩 또는 오류 처리**를 렌더링 초반부에 추가합니다.
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

    // const getActorDetails = (actorId) => {
    //     return actors.find(actor => actor.id === actorId);
    //   };
    
    // const getCharacterDetails = (actorId) => {
    //     return movieActors.find(ma => ma.actor === actorId && ma.movie === movie.id);
    // };

    // const filteredCredits = movie.filter(actorId => {
    //     const actor = getActorDetails(actorId);
    //     return actor && (actor.known_for_department === 'Acting' || actor.known_for_department === 'Directing');
    //   });

  const handleShowAllActors = () => {
    setShowAllActorsPopup(true);
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
                // Redux 상태 업데이트
                dispatch(setScope({ movieId, scope: ratingValue }));
    
                // 성공 메시지 출력
                const message =
                    response.status === 201
                        ? '별점이 생성되었습니다.'
                        : '별점이 생성되었습니다.';
                alert(message);
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

    //   const handleSaveRating = async (movieId, ratingValue) => {
    //     try {
    //         if (!accessToken) {
    //             throw new Error('No access token found. Please log in.');
    //         }
    
    //         const response = await axios.post(
    //             `/api/scope/${movieId}/${userId}`,
    //             { scope: ratingValue }, // 서버로 보낼 데이터
    //             {
    //                 headers: {
    //                     Authorization: `Bearer ${accessToken}`,
    //                     'Content-Type': 'application/json',
    //                 },
    //             }
    //         );
    
    //         if (response.status === 200 || response.status === 201) {
    //             // Redux 상태 업데이트
    //             // dispatch(addRating({ movieId, scope: ratingValue }));
    //             console.log('Redux after dispatch:', scope); // Redux 상태 확인
    //             // 별점 저장 후 데이터를 다시 가져옴
    //             // fetchMovieRating(movieId);
    //             alert('별점이 저장되었습니다.');
    //         } else {
    //             alert('별점 저장에 실패했습니다.');
    //         }
    //     } catch (error) {
    //         console.error('Error saving rating:', error.message);
    //         alert('별점 저장 중 오류가 발생했습니다.');
    //     }
    // };
    
    
    //   const handleSaveRating = async (movieId, scope) => {

    //     console.log('movieId:', movieId); // 예: 101
    //     console.log('rating:', scope);   // 예: 4.5
    //     console.log('userId:', userId);
    //     console.log('Updated Redux Scope:', scope);

    //     try {
    //         // 1. 토큰 확인
    //         if (!accessToken) {
    //             throw new Error('No access token found. Please log in.');
    //         }
    
    //         // 2. Redux 상태에서 기존 별점 확인
    //         const existingRating = scope[movieId];
    
    //         let response;
    //         if (existingRating !== null) {
    //             // 3. 기존 별점이 있으면 수정 (PUT 요청)
    //             response = await axios.post(
    //                 `/api/scope/${movieId}/${userId}`,
    //                 { scope },
    //                 // { rating, movie: movieId, user: userId },
    //                 {
    //                     headers: {
    //                         Authorization: `Bearer ${accessToken}`,
    //                         'Content-Type': 'application/json',
    //                     },
    //                 }
    //             );
    //         } else {
    //             // 4. 기존 별점이 없으면 생성 (POST 요청)
    //             response = await axios.post(
    //                 `/api/scope/${movieId}/${userId}`,
    //                 { scope },
    //                 // { rating, movie: movieId, user: userId },
    //                 {
    //                     headers: {
    //                         Authorization: `Bearer ${accessToken}`,
    //                         'Content-Type': 'application/json',
    //                     },
    //                 }
    //             );
    //         }
    
    //         // 5. 서버 요청 성공 시 Redux 상태 업데이트
    //         if (response.status === 200 || response.status === 201) {
    //             dispatch(addRating({ movieId, scope })); // Redux 상태 업데이트
    //             console.log("redux 확인: ", scope);
    //             alert(existingRating ? '별점이 수정되었습니다.' : '별점이 생성되었습니다.');
    //         } else {
    //             alert('별점 저장에 실패했습니다.');
    //         }
    //     } catch (error) {
    //         // 6. 에러 처리
    //         console.error('Error saving rating:', error.response ? error.response.data : error.message);
    //         if (error.response?.status === 401) {
    //             alert('인증 오류: 로그인 상태를 확인하세요.');
    //         } else {
    //             alert(`별점 저장 중 오류가 발생했습니다: ${error.response ? JSON.stringify(error.response.data) : error.message}`);
    //         }
    //     }
    // };


    

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
                                {/* <StarDisplay rating={selectedRating} /> */}

                                <StarDisplay rating={movieRating} />
                                {/* <StarDisplay rating={typeof movieRating === 'number' ? movieRating : 0} /> */}
                                {console.log("movieRating:", movieRating)}
                                <p>평균별점</p>
                            </div>
                            <Button label="별점" onClick={() => setDialogVisible('scope')} />
                            <Button label="리뷰" onClick={() => setDialogVisible('review')} />
                            <Button label="관람일자" onClick={() => setDialogVisible('viewingDate')} />
                        </div>
                        <div className="movie-overview">
                            <h2>줄거리</h2>
                            <p>{movie.plot}</p>
                        </div>
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
                    {/* <h2>별점</h2> */}
                    {/* StarRating 컴포넌트를 사용해 별점 표시 */}
                    <StarRating onRatingSelect={handleSelectRating} initialRating={selectedRating} />
                    <div className="popup-buttons" style={{ marginTop: '1rem', display: 'flex', gap: '1rem' }}>
                        <Button
                        label="취소"
                        onClick={() => setDialogVisible(null)} // 다이얼로그 닫기
                        style={{
                            padding: '0.5rem 1rem',
                            backgroundColor: '#ccc',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                        }}
                    />
               
                    <Button
                        label="확인"
                        // onClick={handleSaveRating} // 별점 저장 함수 호출
                        onClick={() => handleSaveRating(movieId, selectedRating)} 
                        style={{
                            padding: '0.5rem 1rem',
                            backgroundColor: '#007bff',
                            color: 'white',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                        }}
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
                        value={review}
                        onChange={(e) => setReview(e.target.value)}
                        placeholder="리뷰 입력"
                        rows={5}
                        cols={50}
                        style={{ width: '100%', height: '150px' }} // 원하는 크기
                    />
                </div>
                <div className="review-button">
                    <Button label="취소" />

                    <Button label="확인" />
                </div>
            </Dialog>

            {/* 관람일자 다이얼로그 */}
            <Dialog
                header="관람일자 입력"
                visible={dialogVisible === 'viewingDate'}
                rows={5} cols={30}
                onHide={() => setDialogVisible(null)}
                style={{ width: '50vw' }}
                >
                <div className="dialog-content">
                    <p>관람일자를 입력해주세요:</p>
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
                        {actors.slice(0, 8).map(actor => (
                            <div className="cast-item" key={actor.actor_id}>
                                <img src={`${baseURL}${actor.profile_image}`} />

                                <p>{actor.name}</p>
                            </div>
                        ))}
                    </div>
                    {actors.length > 8 && (
                        <Button label="더보기" className="more-button" onClick={handleShowAllActors} />
                    )}
                </>
                ) : (
                <p>Loading actors...</p>
                )}
            </section>
            <section className="movie-stills">
                <h2>스틸컷{movie.stills.length}</h2>
                <div className="still-list">
                    {/* {movie.stills.slice(0, 4).map((still, index) => (
                    <Image
                        src={`${baseURL}${still}`}
                        alt={`Still ${index + 1}`}
                        key={index}
                        // onClick={() => handleStillClick(`${baseURL}${still}`)}
                        style={{ cursor: 'pointer' }}
                        preview
                    />
            ))} */}

                <Carousel
                    value={movie.stills.slice(0, 4)} // 최대 4개만 표시
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
            <div className="trailer-list">
                {Array.isArray(movie.trailers) && movie.trailers.length > 0 ? (
                    movie.trailers.map((trailer, index) => (
                        <iframe 
                            key={index}
                            width="300"
                            height="169"
                            src={trailer}
                            title={`Movie Trailer ${index + 1}`}
                            frameBorder="0"
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                            allowFullScreen
                        ></iframe>
                    ))
                ) : (
                    <p>트레일러가 없습니다.</p>
                )}
            </div>
        </section>

    </div>

        
    );
};

export default MovieDetail;


function extractYouTubeKey(url) {
    const urlObj = new URL(url);
    return urlObj.searchParams.get('v');
  }