package com.igorbrodevic.spring;

import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages={"com.igorbrodevic"})
@EnableVaadin
public class CustomConfiguration {

/*// this is working but i want to use componentscan!
@Bean
public String test() {
    return "test...";
}

@Bean
public TestBean testBean() {
    return new TestBean();
}

@Bean
public LoginScreen loginScreenBean() {
    return new LoginScreen();*/

}