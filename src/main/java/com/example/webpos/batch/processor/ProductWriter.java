package com.example.webpos.batch.processor;

import com.example.webpos.model.entity.Product;
import com.example.webpos.model.repository.ProductRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductWriter implements ItemWriter<Product> {
    private final ProductRepository repository;

    public ProductWriter(ProductRepository repository){
        this.repository = repository;
    }

    @Override
    public void write(List<? extends Product> list) throws Exception {
        repository.saveAll(list);
    }
}
