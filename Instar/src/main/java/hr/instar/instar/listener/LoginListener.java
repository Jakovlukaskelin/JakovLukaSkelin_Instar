package hr.instar.instar.listener;


import hr.instar.instar.doamin.LoginHistory;
import hr.instar.instar.repository.StoreRepository;
import jakarta.servlet.ServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class LoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final StoreRepository storeRepository;
    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        String username = authentication.getName();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        ServletRequest servletRequest = attributes.getRequest();
        String clientIpAddress = servletRequest.getRemoteAddr();

        LoginHistory loginHistory = new LoginHistory(
                null,
                username,
                LocalDateTime.now().toString(),
                clientIpAddress
        );
        storeRepository.addLoginHistory(loginHistory);
    }
}
