package com.github.euerlcs.demo.batch._04_param_runid_incr;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class IncrementParamJob01 {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;


	@Bean
	@StepScope	//延时获取
	Tasklet tasklet0401() {
		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();

				Object runid = jobParameters.get("run.id");
				System.out.println("param---job--run.id..." + runid);
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	Step step0401() {
		return stepBuilderFactory.get("step1").tasklet(tasklet0401()).build();
	}

	@Bean
	Job job0401() {
		return jobBuilderFactory.get("increate-param-job")
				.start(step0401())
				.incrementer(new RunIdIncrementer())
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(IncrementParamJob01.class, args);
	}

}
