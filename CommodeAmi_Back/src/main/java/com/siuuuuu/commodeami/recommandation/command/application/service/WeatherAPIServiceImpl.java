package com.siuuuuu.commodeami.recommandation.command.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siuuuuu.commodeami.common.exception.CommonException;
import com.siuuuuu.commodeami.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class WeatherAPIServiceImpl implements WeatherAPIService {

    @Value("${openWeatherMap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherAPIServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getWeatherCondition(double latitude, double longitude) {
        log.info("현재 사용 중인 API 키: {}", apiKey);
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&units=metric";

        log.info("날씨 API 요청: {}", url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
//                log.error("날씨 API 오류: {}", response.getStatusCode());
                throw new CommonException(ErrorCode.WEATHER_API_LIST_BAD_REQUEST);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String weather = root.path("weather").get(0).path("main").asText().toLowerCase();

            log.info("가져온 날씨 정보: {}", weather);
            return weather;
        } catch (Exception e) {
//            log.error("날씨 정보를 가져오는 중 오류 발생: {}", e.getMessage());
            throw new CommonException(ErrorCode.WEATHER_API_REQUEST_FAILED);
        }
    }

}
