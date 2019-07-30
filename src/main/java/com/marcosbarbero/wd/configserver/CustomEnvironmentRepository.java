package com.marcosbarbero.wd.configserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class CustomEnvironmentRepository implements EnvironmentRepository, Ordered {

    @Value("${native.file.location}")
    String nativeFileLocation;


    @Autowired @Qualifier("NativeProperties")
    PropertyFinder propertyFinder;

    @Override
    public Environment findOne(String application, String profile, String label) {
        String[] profiles = StringUtils.commaDelimitedListToStringArray(profile);
        Environment environment = new Environment(application,  profiles);
        Properties props = new Properties();
        try {
           props =  propertyFinder.findProperties(application,Profile.valueOf(profile));
            environment.add(new PropertySource(application, props));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*if(profiles[0].equals("dev")) {
            props.setProperty("client.pseudo.property", "dev value");
        }else if(profiles[0].equals("qa")) {
            props.setProperty("client.pseudo.property", "qa vaule");
        }
        environment.add(new PropertySource("application-dev", props));*/

        return environment;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

