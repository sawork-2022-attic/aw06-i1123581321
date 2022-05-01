package com.example.webpos.batch.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;


import java.io.*;

public class JsonFileReader implements ItemReader<JsonNode> {

    private final ObjectMapper mapper;
    private final BufferedReader reader;

    public JsonFileReader(String file) throws FileNotFoundException {
        if (file == null){
            throw new FileNotFoundException();
        }
        if(file.matches("^file:(.*)")){
            file = file.substring(file.indexOf(":") + 1);
        }
        String filename = file;
        reader = new BufferedReader(new FileReader(filename));
        mapper = new ObjectMapper();
    }


    @Override
    public JsonNode read() throws UnexpectedInputException, ParseException, NonTransientResourceException, IOException {
        String line = reader.readLine();
        if (line != null){
            return mapper.readTree(line);
        }
        return null;
    }
}
