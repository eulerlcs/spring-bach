package com.github.euerlcs.demo.batch._02_params_03;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class ParamsJob3 {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	@StepScope	//延时获取
	Tasklet tasklet0203(@Value("#{jobParameters['name']}") String name) {
		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("param---job..." + name);
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	Step step0203() {
		return stepBuilderFactory.get("step1").tasklet(tasklet0203(null)).build();
	}

	@Bean
	Job job0203() {
		return jobBuilderFactory.get("param-value-job").start(step0203()).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(ParamsJob3.class, args);
	}

}
