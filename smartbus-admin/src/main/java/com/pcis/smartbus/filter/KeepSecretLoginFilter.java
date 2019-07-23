package com.pcis.smartbus.filter;

import com.pcis.smartbus.common.Constant;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebFilter(filterName = "keepSecretLoginFilter",
        urlPatterns = {"/api/*", "/images/*", "/system/*"},
        initParams = {
                //@WebInitParam(name = "loginUI", value = "/login/login.html"),
                @WebInitParam(name = "loginProcess", value = "/api/user/login"),
                @WebInitParam(name = "encoding", value = "utf-8")
        })
public class KeepSecretLoginFilter implements Filter {
    private FilterConfig config;

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        // 获取配置参数
        //String loginUI = config.getInitParameter("loginUI");
        String loginProcess = config.getInitParameter("loginProcess");
        String encoding = config.getInitParameter("encoding");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 设置请求的字符集（post请求方式有效）
        request.setCharacterEncoding(encoding);

        // 不带http://域名:端口的地址
        String uri = request.getRequestURI();
        //System.out.println("KeepSecretLoginFilter:" + uri);
        if (uri.equals(loginProcess)) {
            // 登录的请求，放行
            chain.doFilter(request, response);
        }else if (request.getSession().getAttribute(Constant.USER_ID) != null) {
            // 已经登录，放行
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        this.config = null;
    }
}