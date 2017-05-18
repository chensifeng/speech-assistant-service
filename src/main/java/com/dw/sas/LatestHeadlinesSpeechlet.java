package com.dw.sas;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import org.springframework.stereotype.Service;

/**
 * Created by hofmannro on 17.05.2017.
 */
@Service
public class LatestHeadlinesSpeechlet implements Speechlet {

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("LatestHeadlines".equals(intentName)) {
            return getLatestHeadlines(intent);
        } else if ("LatestHeadlinesFromKeyword".equals(intentName)) {
            return LatestHeadlinesFromKeyword(intent); }
        else if ("LatestHeadlinesFromCategory".equals(intentName)) {
            return LatestHeadlinesFromCategory(intent); }
        else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    private SpeechletResponse LatestHeadlinesFromKeyword(final Intent intent) {
        String speechText = "Latest headlines from " + intent.getSlot("keywordslot");

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("LatestHeadlinesFromKeyword");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    private SpeechletResponse LatestHeadlinesFromCategory(final Intent intent) {
        String speechText = "Latest headlines from " + intent.getSlot("categoryslot");

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("LatestHeadlinesFromCategory");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        // any cleanup logic goes here
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse() {
        String speechText = "Hi, this is DW. What news topic are you interested in?";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("DW Welcome.");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getLatestHeadlines(final Intent intent) {
        String speechText = "Latest headlines from " + intent.getSlot("newsslot");

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("getLatestHeadlines");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText = "You can ask for the latest headlines!";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Help");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}
