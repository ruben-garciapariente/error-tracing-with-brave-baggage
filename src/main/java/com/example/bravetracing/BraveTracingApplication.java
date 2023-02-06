package com.example.bravetracing;

import io.micrometer.context.ContextSnapshot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BraveTracingApplication {

	// Example of Async Servlets setup
	@Configuration(proxyBeanMethods = false)
	static class AsyncConfig implements WebMvcConfigurer {
		@Override
		public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
			configurer.setTaskExecutor(new SimpleAsyncTaskExecutor(r -> new Thread(ContextSnapshot.captureAll().wrap(r))));
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(BraveTracingApplication.class, args);
	}

}
