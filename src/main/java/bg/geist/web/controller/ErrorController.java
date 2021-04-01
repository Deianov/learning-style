package bg.geist.web.controller;


import bg.geist.exception.ControllerExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);


    // commented in ApplicationSecurityConfig -> redirect to login
    // AuthenticationException and AccessDeniedException
    @RequestMapping(path = "/forbidden", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView accessDeniedHandler(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("status", "forbidden");
        modelAndView.addObject("statusCode", "403");
        modelAndView.addObject("url", request.getRequestURI());
        modelAndView.setViewName(ControllerExceptionHandler.DEFAULT_ERROR_VIEW);
        return modelAndView;
    }
}
