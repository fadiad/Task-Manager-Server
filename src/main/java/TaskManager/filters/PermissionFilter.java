package TaskManager.filters;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

@Order(2)
@Component
@ComponentScan
public class PermissionFilter extends OncePerRequestFilter implements Ordered {


    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    public PermissionFilter(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("hi in filrer 2");

        try {
           HandlerExecutionChain handler= requestMappingHandlerMapping.getHandler(request);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           if(handler != null){
               HandlerMethod handlerMethod = (HandlerMethod) handler.getHandler();
               if(handlerMethod.hasMethodAnnotation(PreAuthorize.class)) {

               }

           }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}