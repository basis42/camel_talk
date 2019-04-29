package de.guj.dvs.camel;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.lang.Nullable;
import io.micrometer.graphite.GraphiteConfig;
import io.micrometer.graphite.GraphiteMeterRegistry;
import io.micrometer.graphite.GraphiteProtocol;
import org.apache.camel.component.micrometer.MicrometerConstants;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.camel.spring.javaconfig.Main;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * A Camel Application
 */
@Configuration
@ComponentScan
public class MainApp extends CamelConfiguration {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.setConfigClass(MainApp.class);
        main.run();
    }


    /**
     * Set up registry. When using Spring Boot, this is provided for you, and you have to provide
     * a bean of type MeterRegistryCustomizer in order to apply common tags, filters, etc.
     */
    @Bean(name = MicrometerConstants.METRICS_REGISTRY_NAME)
    public GraphiteMeterRegistry meterRegistry() {

        GraphiteConfig graphiteConfig = new GraphiteConfig() {
            @Override
            public Duration step() {
                return Duration.ofSeconds(5);
            }

            @Override
            public GraphiteProtocol protocol() {
                return GraphiteProtocol.PLAINTEXT;
            }

            @Override
            public int port(){
                return 2003;
            }

            @Override
            @Nullable
            public String get(String k) {
                return null;
            }
        };

        return new GraphiteMeterRegistry(graphiteConfig, Clock.SYSTEM);
    }
}

