package com.project.innoutburger.filter;

import com.alibaba.fastjson2.JSON;
import com.project.innoutburger.common.BaseContext;
import com.project.innoutburger.common.R;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/*
* 檢查用戶是否已經完成登錄
* */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路徑匹配器, 支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        将 ServletRequest 转换为 HttpServletRequest 以便访问 HTTP 特定的属性和方法。
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 獲取本次請求的 URI
        String requestURI = request.getRequestURI(); // /backend/index.html

        log.info("攔截到請求: {}", requestURI);

        // 定義不需要處理的請求路徑
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

        // 2. 判斷本次請求需不需要處理
        boolean check = check(urls, requestURI);

        // 3. 如果不需要處理, 則直接放行
        if (check) {
            log.info("本次請求{}不需要處裡", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // 4. 判斷登錄狀態, 如果已經登錄則直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用戶已登錄, 用戶id為: {}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            long id = Thread.currentThread().threadId();
            log.info("線程 id 為: {}", id);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("用戶未登錄");
        // 5. 如果未登錄則返回未登錄結果, 通過輸出流的方式向客戶端 response 數據
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }


    /*
    * 路徑匹配, 檢查本次請求是否需要放行
    * @param urls
    * @param requestURI
    * @return boolean
    * */
    public boolean check(String[] urls, String requestURI) {
        for (String url: urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
