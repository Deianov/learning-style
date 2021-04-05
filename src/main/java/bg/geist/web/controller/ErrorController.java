package bg.geist.web.controller;

import bg.geist.exception.ControllerExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @GetMapping("/403")
    public ModelAndView handleError403(@RequestParam(required = false) String url){
        ModelAndView mav = new ModelAndView();
        mav.addObject("status", HttpStatus.FORBIDDEN);
        mav.addObject("statusCode", HttpStatus.FORBIDDEN.value());
        mav.addObject("url", url);
        mav.addObject("message", "HTTP Status 403 - Access is denied");
        mav.addObject("exception", "AccessDeniedException handled by CustomAccessDeniedHandler");
        mav.setViewName(ControllerExceptionHandler.DEFAULT_ERROR_VIEW);
        return mav;
    }
}