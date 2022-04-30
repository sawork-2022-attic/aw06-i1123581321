package com.example.webpos.batch.config;

import com.example.webpos.batch.processor.JsonFileReader;
import com.example.webpos.batch.processor.ProductProcessor;
import com.example.webpos.batch.processor.ProductWriter;
import com.example.webpos.model.entity.Product;
import com.example.webpos.model.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ProductRepository repository;

    @Autowired
    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, ProductRepository repository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.repository = repository;
    }

    @Bean
    protected Step processProducts(ItemReader<JsonNode> reader, ItemProcessor<JsonNode, Product> processor, ItemWriter<Product> writer) {
        return stepBuilderFactory.get("processProducts")
                .<JsonNode, Product>chunk(20)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job chunksJob() throws FileNotFoundException {
        return jobBuilderFactory
                .get("chunksJob")
                .start(processProducts(itemReader(), itemProcessor(), itemWriter()))
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(20);
        return executor;
    }

    @Bean
    public ItemReader<JsonNode> itemReader() throws FileNotFoundException {
        return new JsonFileReader("C:\\Users\\qian\\eccentric\\workspace\\java\\aw06-i1123581321\\src\\main\\resources\\data\\meta_Magazine_Subscriptions_100-02.json");
    }

    @Bean
    public ItemProcessor<JsonNode, Product> itemProcessor(){
        return new ProductProcessor();
    }


    @Bean
    public ItemWriter<Product> itemWriter(){
        return new ProductWriter(repository);
    }
}
