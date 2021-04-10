package bg.geist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class GeistApplication {
	public static void main(String[] args) {
		SpringApplication.run(GeistApplication.class, args);
	}
}