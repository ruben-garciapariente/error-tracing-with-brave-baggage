package com.example.bravetracing;

import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagationConfig;
import brave.baggage.BaggagePropagationCustomizer;
import brave.baggage.CorrelationScopeConfig;
import brave.baggage.CorrelationScopeCustomizer;
import brave.baggage.CorrelationScopeDecorator;
import io.micrometer.context.ContextSnapshot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BraveTracingApplication {

	private static final BaggageField TEST1 = BaggageField.create("TEST1");

	@Bean
	CorrelationScopeCustomizer baseBaggageFieldsCorrelationScope() {
		return (CorrelationScopeDecorator.Builder builder) -> builder
				.add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(TEST1).flushOnUpdate()
						.build());
	}

	@Bean
	BaggagePropagationCustomizer baseBaggagePropagation() {
		return (BaggagePropagation.FactoryBuilder factoryBuilder) -> factoryBuilder
				.add(BaggagePropagationConfig.SingleBaggageField.remote(TEST1));
	}

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
