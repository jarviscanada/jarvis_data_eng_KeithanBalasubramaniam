package ca.jrvs.apps.twitter.spring;


import ca.jrvs.apps.twitter.TwitterCLIApp;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = "ca.jrvs.apps.twitter") //scan through the package to get @Component
public class TwitterCLIComponentScan {

    public static void main(String[] args) {
        //IoC container
        ApplicationContext context = new AnnotationConfigApplicationContext(
                TwitterCLIComponentScan.class);
        TwitterCLIApp app = context.getBean(TwitterCLIApp.class);

        app.run(args);
    }
}
