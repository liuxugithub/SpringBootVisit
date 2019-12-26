package godday.xin.sixin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = {"godday.xin"})
@MapperScan(basePackages = {"godday.xin.sixin.mapper"})
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx=SpringApplication.run(Application.class, args);
        System.out.println("端口:"+ctx.getEnvironment().getProperty("spring.datasource.hikari.maximum-pool-size"));
    }
}
