package TaskManager.filters;

import TaskManager.entities.SecurityUser;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.service.PermissionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Order(2)
@Component
@ComponentScan
public class PermissionFilter extends OncePerRequestFilter implements Ordered {


    private final RequestMappingHandlerMapping requestMappingHandlerMapping;


    private final PermissionService permissionService;

    @Autowired
    public PermissionFilter(RequestMappingHandlerMapping requestMappingHandlerMapping,PermissionService permissionService) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.permissionService=permissionService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!request.getRequestURI().startsWith("/auth")) {

            try {
                HandlerExecutionChain handler = requestMappingHandlerMapping.getHandler(request);
                if (handler != null && handler.getHandler() instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler.getHandler();
                    if (handlerMethod.hasMethodAnnotation(PreAuthorize.class)) {
                        int boardId = Integer.parseInt(request.getParameter("boardId"));
                        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
                        UserRole authority = permissionService.getUserPermission(boardId, securityUser.getUser().getId());
                        authentication = new UsernamePasswordAuthenticationToken(securityUser, null,Collections.singletonList(new SimpleGrantedAuthority(authority.name())));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        request.setAttribute("boardId",boardId);
                        request.setAttribute("role",authority);
                    }

                }

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Messing args");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}