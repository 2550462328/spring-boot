package cn.ictt.zhanghui.springboot_test.filter;

/**
 * 基于shiro的登录验证拦截
 * @author ZhangHui
 * @date 2020/9/11
 */
//public class FormLoginFilter extends PathMatchingFilter {
//    @Override
//    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//        Subject subject = SecurityUtils.getSubject();
//        boolean isAuthenticated = subject.isAuthenticated();
//        if (!isAuthenticated) {
//            HttpServletResponse resp = (HttpServletResponse) response;
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resp.getWriter().print("NO AUTH!");
//            return false;
//        }
//        return true;
//    }
//}