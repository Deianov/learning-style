package bg.geist.web.interceptors;

import bg.geist.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class UserInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(UserInterceptor.class);


    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {

        if (modelAndView != null && isAuthenticated()) {
            String viewName = modelAndView.getViewName();
            if (viewName == null || viewName.startsWith("redirect:/")) {
                return;
            }
            if (modelAndView.getModel().containsKey(Constants.PROFILE_KEY)) {
                return;
            }
            modelAndView.addObject(Constants.PROFILE_KEY, request.getSession().getAttribute(Constants.PROFILE_KEY));
            logger.info("add profile attributes to view: " + viewName);
        }
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}
