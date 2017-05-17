package com.dw.sas;

import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hofmannro on 17.05.2017.
 */
@RestController
public class AlexaController {
    @RequestMapping(value="/alexa-helloworld",
            method= RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpeechletResponseEnvelope> alexa(Model model) {

        String speechText = "Hello, World.  I am a Spring Boot custom skill.";

        SimpleCard card = new SimpleCard();
        card.setTitle("HelloWorld");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);


        SpeechletResponse response = SpeechletResponse.newTellResponse(speech, card);


        SpeechletResponseEnvelope envelope = new SpeechletResponseEnvelope();
        envelope.setResponse(response);
        envelope.setVersion("1.0");
        envelope.setSessionAttributes(null);

        return new ResponseEntity<SpeechletResponseEnvelope>(envelope, HttpStatus.OK);
    }
}
