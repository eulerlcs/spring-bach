package com.github.euerlcs.demo.batch._02_params_02;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class ParamsJob2 {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	Tasklet tasklet0202() {
		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
				System.out.println("param---job..." + jobParameters.get("name"));
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	Step step0202() {
		return stepBuilderFactory.get("step1").tasklet(tasklet0202()).build();
	}

	@Bean
	Job job0202() {
		return jobBuilderFactory.get("param-job-2").start(step0202()).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(ParamsJob2.class, args);
	}

}
