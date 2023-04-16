package com.github.euerlcs.demo.batch._03_param_validator_03;

import java.util.Arrays;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
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
public class ParamValidatorJob03 {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

    @Bean
    NameParamValidator nameParamValidator() {
		return new NameParamValidator();
	}

	@Bean
	DefaultJobParametersValidator defaultJobParametersValidator() {
		DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
		validator.setRequiredKeys(new String[] { "name" });
		validator.setOptionalKeys(new String[] { "age" });

		return validator;
	}

	@Bean
	CompositeJobParametersValidator compositeJobParametersValidator() throws Exception {
		CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
		validator.setValidators(Arrays.asList(nameParamValidator(), defaultJobParametersValidator()));

		validator.afterPropertiesSet();

		return validator;
	}

	@Bean
	@StepScope // 延时获取
	Tasklet tasklet0303() {
		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
				String name = (String) jobParameters.get("name");
				String age = (String) jobParameters.get("age");
				System.out.println("param---job..." + name);
				System.out.println("param---job..." + age);
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	Step step0303() {
		return stepBuilderFactory.get("step1").tasklet(tasklet0303()).build();
	}

	@Bean
	Job job0303() throws Exception {
		return jobBuilderFactory.get("composite-validator-job").start(step0303())
				.validator(compositeJobParametersValidator()).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(ParamValidatorJob03.class, args);
	}

}
