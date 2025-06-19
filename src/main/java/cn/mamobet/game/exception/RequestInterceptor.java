//package cn.mamobet.game.exception;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Slf4j
//@Component
//public class RequestInterceptor implements HandlerInterceptor {
//
//    /**
//     * 请求前拦截
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String uri = request.getRequestURI();
//
//        // 自动放行 Swagger 路径
//        if (uri.startsWith("/swagger-ui")
//                || uri.startsWith("/v3/api-docs")
//                || uri.startsWith("/swagger-resources")
//                || uri.startsWith("/webjars")) {
//            return true;
//        }
//        log.info("拦截到请求: {} {}", request.getMethod(), uri);
//        return true;
//
//    }
//}