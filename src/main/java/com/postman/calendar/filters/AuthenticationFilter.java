package com.postman.calendar.filters;

import com.postman.calendar.services.AuthenticationService;
import com.postman.calendar.services.UserService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/calendar/defineSlots",
            "/calendar/availableSlots",
            "/calendar/bookedSlots",
            "/calendar/bookSlot"})
public class AuthenticationFilter implements Filter {
    public static final String AUTHENTICATION_HEADER = "Authorization";

    private UserService userService;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String authCredentials = httpServletRequest
                    .getHeader(AUTHENTICATION_HEADER);

            AuthenticationService authenticationService = new AuthenticationService();
            if(userService==null){
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                userService = webApplicationContext.getBean(UserService.class);
            }
            boolean authenticationStatus = authenticationService
                    .authenticate(authCredentials, userService);

            if (authenticationStatus) {
                filter.doFilter(request, response);
            } else {
                if (response instanceof HttpServletResponse) {
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Forbidden For The User");
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                }
            }
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
