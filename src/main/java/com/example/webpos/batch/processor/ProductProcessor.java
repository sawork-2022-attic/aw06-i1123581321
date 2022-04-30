package com.example.webpos.batch.processor;

import com.example.webpos.model.entity.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemProcessor;

public class ProductProcessor implements ItemProcessor<JsonNode, Product> {
    private final ObjectMapper mapper;

    public ProductProcessor(){
        mapper = new ObjectMapper();
    }

    @Override
    public Product process(JsonNode jsonNode) throws Exception {
        if (jsonNode == null){
            return  null;
        }
        var id = jsonNode.get("asin").asText();
        var name = jsonNode.get("title").asText();
        var image = jsonNode.get("imageURLHighRes");
        String imageurl = "";
        if (image.isArray() && image.size() > 0){
            imageurl = image.get(0).asText();
        }
        return new Product(id, name, imageurl);
    }
}
