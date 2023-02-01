package com.example.alexa.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.example.alexa.services.HandlerService;
import com.example.alexa.servicesImpl.HandlerServicesImpl;
import com.example.alexa.utils.Utils;

import javax.swing.text.html.Option;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

public class PlayMusicHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("PlayMusic"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        HandlerService service = new HandlerServicesImpl();
        Optional<Response> resp = service.playSelectedMusic(input);
        return resp;
    }
}
