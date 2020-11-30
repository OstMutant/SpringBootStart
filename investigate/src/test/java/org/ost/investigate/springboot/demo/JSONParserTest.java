package org.ost.investigate.springboot.demo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.StreamSupport;

public class JSONParserTest {

    @Test
    public void test(){
        Set<String> set = new HashSet<>();
        set.add("test1");
        set.add("test.test1");

        JsonElement element =
                JsonParser.parseString(
                        "{\"test\":[[{\"test1\":\"123\"},{\"test2\":\"123\"},{\"test\":{\"test\":\"123\"}}]], \"test1\":\"123\"}");
        set.forEach(v-> {
            parse(new LinkedList<>(Arrays.asList(v.split("\\."))), element);
        });

        System.out.println("element - " + element.toString());
    }
    private void parse(LinkedList<String> properties, JsonElement element){
        if(properties.size() == 0){
            return;
        }
        String property = properties.getFirst();
        if(element.isJsonObject()){
            JsonObject jsonObject = element.getAsJsonObject();
            if(jsonObject.has(property)){
                if(jsonObject.get(property).isJsonPrimitive()){
                    jsonObject.addProperty(property,"");
                } else {
                    properties.removeFirst();
                    parse(properties, jsonObject.get(property));
                }
            }
        } else if(element.isJsonArray()){
            StreamSupport.stream(element.getAsJsonArray().spliterator(), false).forEach(v -> parse(properties, v));
        }
    }
}
