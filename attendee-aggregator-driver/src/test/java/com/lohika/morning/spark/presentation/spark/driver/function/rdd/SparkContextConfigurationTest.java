package com.lohika.morning.spark.presentation.spark.driver.function.rdd;

import com.lohika.morning.spark.presentation.spark.driver.location.DataFilesLocation;
import com.lohika.morning.spark.presentation.spark.driver.location.LocalDataFilesLocation;

import java.io.File;
import java.net.URL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:spark-test.properties")
public class SparkContextConfigurationTest {

    static {
        File file = new File(System.getProperty("user.dir"));
        String parentPath = file.getAbsoluteFile().getParent();

        File parentFile = new File(parentPath);

        System.getProperties().setProperty("app.home", parentFile.getAbsoluteFile().getParent());
    }

    @Bean(name = "testDataFilesLocation")
    public DataFilesLocation dataFilesLocation() {
        return new LocalDataFilesLocation() {

            @Override
            public String getPath() {
                URL resource = this.getClass().getResource("/test-data");

                return resource.getPath();
            }

        };
    }

}
