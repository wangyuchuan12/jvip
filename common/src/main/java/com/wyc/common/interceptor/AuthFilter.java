package com.wyc.common.interceptor;
import javax.servlet.*;
import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.print("..........................init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        System.out.print(".........................doFilter");
        filterChain.doFilter(servletRequest,servletResponse);
    }
    @Override
    public void destroy() {
        System.out.print(".........................destroy");
    }
}
