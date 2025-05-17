package com.example.laba10.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@WebFilter("/*")
public class PFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String method = req.getParameter("_method");

        if (method != null && (method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("DELETE"))) {
            HttpServletRequest wrapper = new HttpMethodRequestWrapper(req, method);
            chain.doFilter(wrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
        private final String method;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method.toUpperCase();
        }

        @Override
        public String getMethod() {
            return this.method;
        }
    }
}