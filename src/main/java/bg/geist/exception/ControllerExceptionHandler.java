package bg.geist.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/*
    Important Note: The Model may not be a parameter of any @ExceptionHandler method.
    Instead, setup a model inside the method using a ModelAndView as shown by handleError() above.
*/

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    public static final String DEFAULT_ERROR_VIEW = "error";


    @ExceptionHandler(HttpMessageNotReadableException.class) // json parsing
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handleError400(final HttpMessageNotReadableException ex, final WebRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // http://localhost:8080/api/quiz/-a-
    public ResponseEntity<ErrorMessage> handleError400(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleError404(final ObjectNotFoundException ex, final WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessage> handleError405(final HttpRequestMethodNotSupportedException ex, final WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(SQLException.class)
    public ModelAndView databaseError() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "SQLException");
        modelAndView.setViewName(DEFAULT_ERROR_VIEW);
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(final HttpServletRequest req, final Exception ex) throws Exception {
        logger.error(ex.getMessage(), ex);
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the ObjectNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation
                (ex.getClass(), ResponseStatus.class) != null)
            throw ex;
        // Otherwise setup and send the user to a default error-view.

        Object statusCode = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String status = "";

        if (statusCode != null) {
            statusCode = statusCode.toString();
            status = HttpStatus.valueOf(Integer.parseInt((String) statusCode)).toString();
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("status", status);
        mav.addObject("statusCode", statusCode);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("message", ex.getMessage());
        mav.addObject("exception", ex);
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}

/*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(final Exception ex, final WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR); // HttpStatus 500
    }
*/