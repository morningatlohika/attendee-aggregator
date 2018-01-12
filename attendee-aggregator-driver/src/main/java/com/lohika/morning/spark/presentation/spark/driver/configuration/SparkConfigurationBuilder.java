package com.lohika.morning.spark.presentation.spark.driver.configuration;

import com.lohika.morning.spark.presentation.spark.distributed.library.type.EventsByParticipant;
import com.lohika.morning.spark.presentation.spark.distributed.library.type.Participant;
import com.lohika.morning.spark.presentation.spark.distributed.library.type.ParticipantEmailPosition;
import com.lohika.morning.spark.presentation.spark.distributed.library.type.ParticipantsByCompany;
import java.util.Map;
import org.apache.spark.SparkConf;

/**
 * A Spring friendly builder to work around overloaded scala bean properties in SparkConf.
 */
public class SparkConfigurationBuilder {

    private final String master;
    private final String appName;
    private final String[] jars;
    private final Map<String, String> sparkProperties;

    public SparkConfigurationBuilder(String master, String appName, String[] jars, Map<String, String> sparkProperties) {
        this.master = master;
        this.appName = appName;
        this.jars = jars;
        this.sparkProperties = sparkProperties;
    }

    public SparkConf buildSparkConfiguration() {
        SparkConf sparkConf = new SparkConf()
                .setMaster(master)
                .setAppName(appName == null ? "name-not-set" : appName)
                .setJars(jars)
                .set("spark.cores.max", sparkProperties.get("spark.cores.max"))
                .set("spark.executor.memory", sparkProperties.get("spark.executor.memory"))
                .set("spark.sql.shuffle.partitions", sparkProperties.get("spark.sql.shuffle.partitions"))
                .set("spark.default.parallelism", sparkProperties.get("spark.default.parallelism"))
                .set("spark.serializer", sparkProperties.get("spark.serializer"));

        if (sparkProperties.get("spark.serializer").equals("org.apache.spark.serializer.KryoSerializer")) {
            sparkConf.set("spark.kryo.registrationRequired", "false");

            sparkConf.registerKryoClasses(new Class[]{EventsByParticipant.class,
                                                      Participant.class,
                                                      ParticipantEmailPosition.class,
                                                      ParticipantsByCompany.class
                                                      });
        }

        return sparkConf;
    }
}
