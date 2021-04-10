package bg.geist.aop;

import bg.geist.domain.entity.Log;
import bg.geist.domain.model.binding.UserRoleBindingModel;
import bg.geist.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    private static final String USERS_ADD_ROLE = "id:%d added role:%s";
    private static final String EXERCISES_REST_CONTROLLER = "%s, %s, %s";

    private final LogService logService;

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    @Pointcut("execution(* bg.geist.web.controller.AdminController.roleAdd(..))")
    public void addRolePointcut(){};

    @After("addRolePointcut()")
    public void afterAdd(JoinPoint joinPoint){

        Object[] args = joinPoint.getArgs();
        UserRoleBindingModel bindingModel = (UserRoleBindingModel) args[0];
        String action = String.format(USERS_ADD_ROLE, bindingModel.getId(), bindingModel.getRole());

        logService.createLog(Log.Level.INFO, Log.Tag.USERS.toString(), action);
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllers(){};

    @Pointcut("execution(* *.*(..))")
    protected void allMethod() {
    }

    @Before("restControllers()&& allMethod()")
    public void logBefore(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String method = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
//        String target = joinPoint.getTarget().getClass().getName();
        String action = String.format(EXERCISES_REST_CONTROLLER, className, method, args);
        logService.createLog(Log.Level.INFO, Log.Tag.CONTROLLERS.toString(), action);
    }
}