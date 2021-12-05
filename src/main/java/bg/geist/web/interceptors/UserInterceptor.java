package bg.geist.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bg.geist.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;


@Component
public class UserInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(UserInterceptor.class);

    /**
     * Executed before actual handler is executed
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if (isUserLogged()) {
            addToModelUserDetails(request.getSession());
        }
        return true;
    }

    /**
     * Executed before after handler is executed. If view is a redirect view, we don't need to execute postHandle
     **/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model) throws Exception {

        if (!isRedirectView(model) && isUserLogged()) {
            addToModelUserDetails(model);
        }

        // don't change !
        if (!isRedirectView(model) && isAuthenticated()) {
            if (model.getModel().containsKey(Constants.PROFILE_KEY)) {
                return;
            }
            model.addObject(Constants.PROFILE_KEY, request.getSession().getAttribute(Constants.PROFILE_KEY));
            log.info("add profile attributes to view: " + model.getViewName());
        }
    }

    /**
     * Used before model is generated, based on session
     */
    private void addToModelUserDetails(HttpSession session) {
        log.info("================= addToModelUserDetails ============================");
        String loggedUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        session.setAttribute("username", loggedUsername);
        log.info("user(" + loggedUsername + ") session : " + session);
        log.info("================= addToModelUserDetails ============================");

    }

    /**
     * Used when model is available
     */
    private void addToModelUserDetails(ModelAndView model) {
        log.info("================= addToModelUserDetails ============================");
        String loggedUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        model.addObject("loggedUsername", loggedUsername);
        log.trace("session : " + model.getModel());
        log.info("================= addToModelUserDetails ============================");

    }

    private static boolean isRedirectView(ModelAndView mv) {
        if (mv == null || !mv.hasView() || mv.getViewName() == null) {
            return false;
        }
        if (mv.getViewName().startsWith("redirect:/")) {
            return true;
        }
        View view = mv.getView();
        return (view instanceof SmartView && ((SmartView) view).isRedirectView());
    }

    private static boolean isUserLogged() {
        try {
            return !SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName()
                    .equals("anonymousUser");
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}