package com.ai.aichatbackend.utils;

import cn.hutool.http.Method;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author mac
 */
@SpringBootConfiguration
@Configuration
public class CorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @NotNull FilterChain filterChain) throws ServletException, IOException {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods",
                "POST, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                //允许自定义的请求头
                "Content-Type, x-requested-with, X-Custom-Header, Request-Ajax");
        if (httpServletRequest.getMethod().toUpperCase().equals(Method.OPTIONS.name())) {
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}