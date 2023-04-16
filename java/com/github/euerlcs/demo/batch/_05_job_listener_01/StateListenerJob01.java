package com.github.euerlcs.demo.batch._05_job_listener_01;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class StateListenerJob01 {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	Tasklet tasklet0501() {
		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				BatchStatus status = contribution.getStepExecution().getJobExecution().getStatus();

				System.out.println("starting..." + status);
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	JobStateListener jobStateListener() {
		return new JobStateListener();
	}

	@Bean
	Step step0501() {
		return stepBuilderFactory.get("step1").tasklet(tasklet0501()).build();
	}

	@Bean
	Job job0501() {
		return jobBuilderFactory.get("job-state-listener-job").start(step0501()).listener(jobStateListener())
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(StateListenerJob01.class, args);
	}

}
