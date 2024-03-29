package TaskManager.filters;

import TaskManager.entities.SecurityUser;
import TaskManager.utils.jwtUtils.JWTTokenHelper;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter implements Ordered {

    private final UserDetailsService userDetailsService;
    private final JWTTokenHelper jwtTokenHelper;

    public JWTAuthenticationFilter(UserDetailsService userDetailsService, JWTTokenHelper jwtTokenHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenHelper = jwtTokenHelper;

    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        if(!request.getRequestURI().contains("/auth")) {
            String authToken = jwtTokenHelper.getToken(request);


            if (null != authToken) {
                String userName = jwtTokenHelper.getUsernameFromToken(authToken);

                if (null != userName) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                    if (jwtTokenHelper.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
                        authentication.setDetails(new WebAuthenticationDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        request.setAttribute("userId",((SecurityUser)userDetails).getUser().getId());

                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }

                }else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

            }else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

        }

        filterChain.doFilter(request, response);

    }

    @Override
    public int getOrder() {
        return 1;
    }
}




