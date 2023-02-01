package com.example.alexa.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.audioplayer.PlaybackNearlyFinishedRequest;
import com.amazon.ask.request.Predicates;
import com.example.alexa.services.HandlerService;
import com.example.alexa.servicesImpl.HandlerServicesImpl;

import java.util.Optional;

public class PlayBackNearlyFinishedRequest implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(PlaybackNearlyFinishedRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        HandlerService service = new HandlerServicesImpl();
        Optional<Response> resp = service.playNextIntent(input);
        return resp;
    }
}
