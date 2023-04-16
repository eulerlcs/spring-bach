package com.github.euerlcs.demo.batch._08_tasklet_chunk_01;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class ChunkTaskletJob01 {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	ItemReader itemReader080101() {
		return new ItemReader() {

			@Override
			public Object read()
					throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
				System.out.println("----------read-----aa------");
				return "read-ret";
			}

		};
	}

	@Bean
	ItemProcessor itemProcessor080101() {
		return new ItemProcessor() {

			@Override
			public Object process(Object item) throws Exception {
				System.out.println("----------process-----bb------");
				return "process-ret-->" + item;
			}
		};
	}

	@Bean
	ItemWriter itemWriter080101() {
		return new ItemWriter() {

			@Override
			public void write(List items) throws Exception {
				System.out.println(items);

			}
		};
	}

	@Bean
	Step step080101() {
		return stepBuilderFactory.get("step080101").chunk(3).reader(itemReader080101()).processor(itemProcessor080101())
				.writer(itemWriter080101()).build();
	}

	@Bean
	Job job080101() {
		return jobBuilderFactory.get("tasklet-simple-job-01").start(step080101()).incrementer(new RunIdIncrementer())
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(ChunkTaskletJob01.class, args);
	}

}
