package com.example.bravetracing;

import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagationConfig;
import brave.baggage.BaggagePropagationCustomizer;
import brave.baggage.CorrelationScopeConfig;
import brave.baggage.CorrelationScopeCustomizer;
import brave.baggage.CorrelationScopeDecorator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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

	@Bean(name = {"taskExecutor" , "applicationTaskExecutor"} )
	public ThreadPoolTaskExecutor threadPoolTaskExecutor(TaskDecorator taskDecorator) {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setThreadNamePrefix("Async-->");
		threadPoolTaskExecutor.setTaskDecorator(taskDecorator);

		return threadPoolTaskExecutor;
	}

	public static void main(String[] args) {
		SpringApplication.run(BraveTracingApplication.class, args);
	}

}
