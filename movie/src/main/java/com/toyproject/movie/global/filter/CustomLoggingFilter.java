package com.toyproject.movie.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper; // JSON 예쁘게 찍기용

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 캐싱 래퍼로 감싸기 (Body를 여러 번 읽기 위함)
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, 1024 * 1024);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long end = System.currentTimeMillis();

        // 로그 기록
        logRequest(requestWrapper, end - start);
        logResponse(responseWrapper);

        // 클라이언트에게 응답 바디를 다시 복사
        responseWrapper.copyBodyToResponse();
    }

    private void logRequest(ContentCachingRequestWrapper request, long duration) throws IOException {
        String queryString = request.getQueryString();
        String payload = getPayload(request.getContentAsByteArray());
        String ip = getClientIp(request);
        log.info("\n[REQUEST] {} {} |IP: {} |Time: {}ms | Query: {}\nPayload: {}",
                request.getMethod(),
                request.getRequestURI(),
                duration,
                ip,
                queryString == null ? "None" : queryString,
                formatJson(payload));
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        String payload = getPayload(response.getContentAsByteArray());

        log.info("\n[RESPONSE] Status: {}\nPayload: {}",
                response.getStatus(),
                formatJson(payload));
    }

    private String getPayload(byte[] contents) {
        if (contents.length > 0) {
            // [개선] 바이너리 데이터나 너무 큰 데이터는 로깅에서 제외하는 로직을 추가하면 좋습니다.
            if (contents.length > 5000) { // 예: 5KB 이상은 요약 처리
                return "Payload too large (" + contents.length + " bytes)";
            }
            return new String(contents);
        }
        return "";
    }

    // IP
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 여러 개의 IP가 넘어올 경우 첫 번째 IP를 선택 (X-Forwarded-For: client, proxy1, proxy2)
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    // JSON 포맷팅
    private String formatJson(String json) {
        if (json.isEmpty()) return "empty";
        try {
            Object obj = objectMapper.readValue(json, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return json; // JSON이 아니면 그냥 평문 출력
        }
    }
}