package com.marcosbarbero.wd.configserver;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class YamlBackToProperties {


    public static void main(String[] args) throws IOException {

        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(Paths.get("/Users/gautamverma/native-props/configclient-default.yml"))) {

            TreeMap<String, Map<String, Object>> config = yaml.loadAs(in, TreeMap.class);
            //System.out.println(String.format("%s%n\nConverts to Properties:%n%n%s", config.toString(), toProperties(config)));
            //Properties p = toProperties( toProperties(config));
            //System.out.println(p);
        }
    }


    public static Map<String,Object> getAllProperties(File file) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        try (InputStream in = new FileInputStream(file)) {
            TreeMap<String, Map<String, Object>> config = yaml.loadAs(in, TreeMap.class);
            //Properties p = toProperties( toProperties(config));
            return  toProperties( toProperties(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String,Object> toProperties(String vaules){
            String [] lines = vaules.split("\n");
            Map<String,Object> map = new HashMap<>();
            for(String line:lines){
                String part[] = line.split("=");
                map.put(part[0],part[1]);
            }
            //Properties properties = new Properties();
            //properties.putAll(map);
            return map;
    }

    private static String toProperties(TreeMap<String, Map<String, Object>> config) {
        StringBuilder sb = new StringBuilder();
        for (String key : config.keySet()) {
            sb.append(toString(key, config.get(key)));
        }
        return sb.toString();
    }

    private static String toString(String key, Map<String, Object> map) {

        StringBuilder sb = new StringBuilder();

        for (String mapKey : map.keySet()) {

            if (map.get(mapKey) instanceof Map) {
                sb.append(toString(String.format("%s.%s", key, mapKey), (Map<String, Object>) map.get(mapKey)));
            } else {
                sb.append(String.format("%s.%s=%s%n", key, mapKey, map.get(mapKey).toString()));
            }
        }

        return sb.toString();
    }
}