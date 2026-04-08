package com.example.demo.Security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.demo.Repository.UserRepository;
import com.example.demo.entity.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            log.info("incoming request : ", request.getRequestURI());
            String requestTokenHeader = request.getHeader("Authorization");
            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = requestTokenHeader.split("Bearer ")[1];
            String userName = authUtil.getUsernameFromToken(token);

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByUsername(userName)
                        .orElseThrow(() -> new UsernameNotFoundException("No username found with the name" + userName));
                UsernamePasswordAuthenticationToken _token = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(_token);
            }
            filterChain.doFilter(request, response);// go ahead in the filter chain.
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
// Prior to this when the error handling was not done, we do not have try catch
// block. In that actually what was happening that the exceptions were coming in
// the security filter chains only, not in the controller layer. Using this
// catch block above, we are actually telling to the exception that go out of
// the filter-chain and give us the exception only.