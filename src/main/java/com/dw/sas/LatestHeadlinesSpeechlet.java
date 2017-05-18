package com.dw.sas;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            return getLatestHeadlines(intent, session);
        } else if ("LatestHeadlinesFromKeyword".equals(intentName)) {
            return LatestHeadlinesFromKeyword(intent); }
        else if ("LatestHeadlinesFromCategory".equals(intentName)) {
            return LatestHeadlinesFromCategory(intent); }
        else if ("HearMore".equals(intentName)) {
            return getNextPageOfItems(intent, session); }
        else if ("Next".equals(intentName)) {
            return LatestHeadlinesFromCategory(intent); }
        else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    private SpeechletResponse getNextPageOfItems(Intent intent, Session session) {
        if (session.getAttributes().containsKey("newsslot")) {
            int currentIndex = (Integer) session.getAttribute("newsslot");
            int currentItemNumberInList = currentIndex + 1;
            StringBuilder speechOutput = new StringBuilder();

            // Iterate through the session attributes to create the next n results for the user.
            for (int i = 0; i < 2; i++) {
                String currentString =
                        (String) session.getAttribute(Integer.toString(currentIndex));
                if (currentString != null) {
                    if (currentItemNumberInList < 2) {
                        speechOutput.append("<say-as interpret-as=\"ordinal\">" + currentItemNumberInList
                                + "</say-as>. " + currentString + ". ");
                    } else {
                        speechOutput.append("And the <say-as interpret-as=\"ordinal\">"
                                + currentItemNumberInList
                                + "</say-as> top seller is. " + currentString
                                + ". Those were the 10 top sellers in Amazon's "
                                + session.getAttribute("newsslot") + " department");
                    }
                    currentIndex++;
                    currentItemNumberInList++;
                }
            }

            // Set the new index and end the session if the newIndex is greater than the MAX_ITEMS
            session.setAttribute("newsslot", currentIndex);
                SsmlOutputSpeech output = new SsmlOutputSpeech();
                output.setSsml("<speak>" + speechOutput.toString() + "</speak>");
                return SpeechletResponse.newTellResponse(output);

        }
        return getWelcomeResponse();
    }

    private SpeechletResponse LatestHeadlinesFromKeyword(final Intent intent) {
        String speechText = "Latest headlines from " + intent.getSlot("keywordslot").getValue();

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
        String speechText = "Latest headlines from " + intent.getSlot("categoryslot").getValue();

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
    private SpeechletResponse getLatestHeadlines(final Intent intent, final Session session) {

        List<String> example= new ArrayList<>();
        example.add("US Vice President Pence says 'Trump committed to working with the EU");
        example.add("US defense chief plays down Trump comments on seizing Iraq oil");


        String cardTitle = "Top Sellers for " + "Latest headlines from " + intent.getSlot("newsslot").getValue();;
        StringBuilder cardOutput = new StringBuilder();
        StringBuilder speechOutput = new StringBuilder();
        session.setAttribute("newsslot", "news");

        int i = 0;
        for (String item : example) {
            int numberInList = i + 1;
            if (numberInList == 1) {
                // Set the speech output and current index for just the top item in the list.
                // Other results are paginated based on subsequent user intents
                speechOutput.append("The top seller is: ").append(item).append(". ");
                session.setAttribute("newsslot", numberInList);
            }

            // Set the session attributes and full card output
            session.setAttribute(Integer.toString(i), item);
            cardOutput.append(numberInList).append(". ").append(item).append(".");
            i++;
        }


        SimpleCard card = new SimpleCard();
        card.setContent(cardOutput.toString());
        card.setTitle(cardTitle);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(String.valueOf(speechOutput));

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
