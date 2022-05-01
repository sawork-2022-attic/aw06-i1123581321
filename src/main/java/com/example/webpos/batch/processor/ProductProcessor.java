package com.example.webpos.batch.processor;

import com.example.webpos.model.entity.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemProcessor;

public class ProductProcessor implements ItemProcessor<JsonNode, Product> {


    public ProductProcessor(){

    }

    private String truncate(String s){
        if (s.length() > 255){
            return s.substring(0, 254);
        }
        return s;
    }


    @Override
    public Product process(JsonNode jsonNode) {
        if (jsonNode == null){
            return  null;
        }
        var id = jsonNode.get("asin").asText();
        var name = jsonNode.get("title").asText();
        var image = jsonNode.get("imageURLHighRes");
        String imageUrl = "";
        if (image.isArray() && image.size() > 0){
            imageUrl = image.get(0).asText();
        }
        return new Product(id, name, imageUrl);
    }
}
