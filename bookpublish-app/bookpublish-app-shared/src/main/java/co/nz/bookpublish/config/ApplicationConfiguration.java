package co.nz.bookpublish.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackages = "co.nz.bookpublish")
@ImportResource({"classpath:activitiAppContext.xml"})
public class ApplicationConfiguration {

}
