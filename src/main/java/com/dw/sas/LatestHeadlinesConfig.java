package com.dw.sas;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hofmannro on 17.05.2017.
 */
@Configuration
public class LatestHeadlinesConfig {
    @Autowired
    private LatestHeadlinesSpeechlet mySpeechlet;

    @Bean
    public ServletRegistrationBean registerServlet() {

        SpeechletServlet speechletServlet = new SpeechletServlet();
        speechletServlet.setSpeechlet(mySpeechlet);

        return new ServletRegistrationBean(speechletServlet, "/alexa-latestnews");
    }
}
