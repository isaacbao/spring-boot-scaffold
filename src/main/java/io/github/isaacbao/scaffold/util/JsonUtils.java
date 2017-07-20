package io.github.isaacbao.scaffold.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.isaacbao.scaffold.system.exception.JsonParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class JsonUtils {

    private JsonUtils() {
    }

    private final static ObjectMapper om = new ObjectMapper();

    public static <E> E readValue(InputStream is, Class<E> clazz) throws IOException {
        return om.readValue(is, clazz);
    }

    public static <E> E readValue(File fl, Class<E> clazz) throws IOException {
        return om.readValue(fl, clazz);
    }

    public static <E> E readValue(String str, Class<E> clazz) throws JsonParseException {
        try {
            return om.readValue(str, clazz);
        } catch (IOException e) {
            throw new JsonParseException(e,str);
        }
    }

    public static void writeValue(File fl, Object obj) throws IOException {
        om.writeValue(fl, obj);
    }

    public static String writeValueAsString(Object obj) throws JsonProcessingException {
        return om.writeValueAsString(obj);
    }

    public static void writeValue(OutputStream os, Object obj) throws IOException {
        om.writeValue(os, obj);
    }

    public static <E> Map<String, Object> objectToMap(E e) {
        return om.convertValue(e, Map.class);
    }

    public static <E> E mapToObject(Map map, Class<E> clazz) {
        return om.convertValue(map, clazz);
    }

    public static <E> E readValue(String string, TypeReference<E> typeReference) throws IOException {
        return om.readValue(string, typeReference);
    }

    public static Map<String, String> json2Map(String json) {
        String str = json;
        Map<String, String> result = new HashMap<>();
        str = str.replaceAll("[{|}|'|\"]", "");
        String[] strs = str.split(",");
        for (String ele : strs) {
            String[] entry = ele.split(":");
            result.put(entry[0], entry[1]);
        }
        return result;
    }

}
