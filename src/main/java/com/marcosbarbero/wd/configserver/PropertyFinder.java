package com.marcosbarbero.wd.configserver;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


interface  PropertyFinder {

    Properties findProperties(String appName, Profile profile) throws IOException;

}
