package com.example.springbatch.Config;

import com.example.springbatch.Entity.PersonEntity;
import org.springframework.batch.item.ItemProcessor;

public class PersonProcessor implements ItemProcessor<PersonEntity,PersonEntity> {
    @Override
    public PersonEntity process(PersonEntity item) throws Exception {
        return item;
    }
}
