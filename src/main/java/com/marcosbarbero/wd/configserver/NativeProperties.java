package com.marcosbarbero.wd.configserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

@Component(value = "NativeProperties")
public class NativeProperties implements  PropertyFinder{

    @Value("${native.file.location}")
    String nativeFileLocation;

    @Override
    public Properties findProperties(String appName, Profile profile) throws IOException {

        File dir = new File(nativeFileLocation);
        File[] files = dir.listFiles((d, name) -> name.matches(fileregexpattern(appName,profile)));
        Properties properties = new Properties();
        for(File file:files){
            FileInputStream fileInput = new FileInputStream(file);
            if(file.getName().contains(".yml")){
                properties.putAll(YamlBackToProperties.getAllProperties(file));
            }else {
                properties.load(fileInput);
            }
            fileInput.close();
        }
        return properties;
    }

    public String fileregexpattern(String appName, Profile profile){
        return String.format("^%s-(%s|%s).(%s|%s)",appName,"default",profile,"properties","yml");
        //"^configclient-(default|dev).(properties|yml)$";
    }
}
