package com.example.springbatch.Config;

import com.example.springbatch.Entity.PersonEntity;
import com.example.springbatch.Repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class SpringBatchConfig {

    private PersonRepository personRepository;
    private JobRepository jobRepository;
    private PlatformTransactionManager platformTransactionManager;
    @Bean
    public FlatFileItemReader<PersonEntity>reader()
    {
        FlatFileItemReader<PersonEntity> itemReader=new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/username.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<PersonEntity> lineMapper() {
        DefaultLineMapper<PersonEntity> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(";");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("username","identifier","firstname","lastname");
        BeanWrapperFieldSetMapper<PersonEntity>fieldSetMapper=new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(PersonEntity.class);
        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
    @Bean
    public PersonProcessor processor(){
        return new PersonProcessor();
    }
    @Bean
    public RepositoryItemWriter<PersonEntity>writer(){
        RepositoryItemWriter<PersonEntity>repositoryItemWriter=new RepositoryItemWriter<>();
        repositoryItemWriter.setRepository(personRepository);
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;
    }
    @Bean
    public Step step1()
    {
        return new StepBuilder("csvImport",jobRepository).<PersonEntity,PersonEntity>chunk(10,platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();

    }
    @Bean
    public Job runJob(){
        return new JobBuilder("importperson",jobRepository).start(step1()).build();
    }


}
