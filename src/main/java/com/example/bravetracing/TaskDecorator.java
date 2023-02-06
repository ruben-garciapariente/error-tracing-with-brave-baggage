package com.example.bravetracing;

import io.micrometer.context.ContextSnapshot;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.stereotype.Component;

@Component
public class TaskDecorator implements org.springframework.core.task.TaskDecorator {

	private final ObservationRegistry registry;

	public TaskDecorator(ObservationRegistry registry) {
		this.registry = registry;
	}

	@Override
	public Runnable decorate(Runnable runnable) {

		return ContextSnapshot.captureAll().wrap(() -> {
			runnable.run();

			/*
			Observation observation = Observation.createNotStarted("task-decorator", registry);
			observation.start();

			try (Observation.Scope scope = observation.openScope()) {
				runnable.run();
			}
			finally {
				observation.stop();
			}*/
		});
	}

}
