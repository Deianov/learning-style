package bg.geist.config;

import bg.geist.web.interceptors.FaviconInterceptor;
import bg.geist.web.interceptors.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserInterceptor userInterceptor;
    private final FaviconInterceptor faviconInterceptor;

    public WebConfig(UserInterceptor userInterceptor, FaviconInterceptor faviconInterceptor) {
        this.userInterceptor = userInterceptor;
        this.faviconInterceptor = faviconInterceptor;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // redirect to Swagger
        registry.addViewController("/api").setViewName("redirect:/v2/api-docs");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor);
        registry.addInterceptor(faviconInterceptor);
    }
}