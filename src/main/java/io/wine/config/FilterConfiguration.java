package io.wine.config;

import io.wine.exception.SessionExpiredException;
import io.wine.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter(
            @Qualifier("sessionMap") ConcurrentHashMap<String, User> sessionMap) {
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RequestResponseLoggingFilter(sessionMap));
        registrationBean.addUrlPatterns("/wines/*");
        registrationBean.addUrlPatterns("/orders/*");

        return registrationBean;
    }

    private class RequestResponseLoggingFilter implements Filter {

        private ConcurrentHashMap<String, User> sessionMap;

        RequestResponseLoggingFilter(ConcurrentHashMap<String, User> sessionMap) {
            this.sessionMap = sessionMap;
        }

        @Override
        public void init(FilterConfig filterConfig) {
            //do nothing
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) request;
            String session = req.getHeader("sessionId");

            if (session != null && sessionMap.containsKey(session)) {
                chain.doFilter(request, response);
            } else {
                HttpServletResponse res = (HttpServletResponse) response;
                res.sendError(401, "Session Expired");
                //TODO: make return 401.
            }
        }

        @Override
        public void destroy() {
            // do nothing
        }
    }
}
