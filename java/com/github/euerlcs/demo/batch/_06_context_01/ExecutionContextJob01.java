package com.github.euerlcs.demo.batch._06_context_01;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class ExecutionContextJob01 {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	Tasklet tasklet060101() {
		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// Map<String, Object> stepExecutionContext =
				// chunkContext.getStepContext().getStepExecutionContext();
				ExecutionContext stepEC = chunkContext.getStepContext().getStepExecution().getExecutionContext();
				stepEC.put("key-step1-step", "value-step1-step");

				System.out.println("starting...111............");

				ExecutionContext jobEC = chunkContext.getStepContext().getStepExecution().getJobExecution()
						.getExecutionContext();
				jobEC.put("key-step1-job", "value-step1-job");

				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	Tasklet tasklet060102() {
		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

				ExecutionContext stepEC = chunkContext.getStepContext().getStepExecution().getExecutionContext();
				System.err.println("key-step1-step = " + stepEC.get("key-step1-step"));

				System.out.println("starting...222............");

				ExecutionContext jobEC = chunkContext.getStepContext().getStepExecution().getJobExecution()
						.getExecutionContext();

				System.err.println("key-step1-job = " + jobEC.get("key-step1-job"));

				return RepeatStatus.FINISHED;
			}
		};
	}


	@Bean
	Step step060101() {
		return stepBuilderFactory.get("step060101").tasklet(tasklet060101()).build();
	}

	@Bean
	Step step060102() {
		return stepBuilderFactory.get("step060102").tasklet(tasklet060102()).build();
	}

	@Bean
	Job job0601() {
		return jobBuilderFactory.get("api-excution-context-job-01").start(step060101()).next(step060102())
				.incrementer(new RunIdIncrementer()).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(ExecutionContextJob01.class, args);
	}

}
