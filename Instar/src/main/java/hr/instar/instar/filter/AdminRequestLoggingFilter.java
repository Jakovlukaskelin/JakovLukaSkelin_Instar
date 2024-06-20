 package hr.instar.instar.filter;


import hr.instar.instar.doamin.RequestHistory;
import hr.instar.instar.repository.StoreRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class AdminRequestLoggingFilter implements Filter {
    private StoreRepository storeRepository;

    @Override
    public void init(FilterConfig filterConfig) {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        storeRepository = context.getBean(StoreRepository.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        StringBuilder paramsBuilder = new StringBuilder();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            paramsBuilder.append(entry.getKey()).append("=");
            String[] valueArr = entry.getValue();
            for (int i = 0; i < valueArr.length; i++) {
                paramsBuilder.append(valueArr[i]);
                if (i < valueArr.length - 1) {
                    paramsBuilder.append(",");
                }
            }
            paramsBuilder.append("; ");
        }

        String requestInfo = String.format("Endpoint: %s | Method: %s | Parameters: %s",
                requestURI, method, paramsBuilder);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int requestType = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        RequestHistory requestHistory = new RequestHistory(
                null, username, formattedDateTime, requestInfo, requestType
        );

        storeRepository.addRequestHistory(requestHistory);

        chain.doFilter(request, response);
    }
}

