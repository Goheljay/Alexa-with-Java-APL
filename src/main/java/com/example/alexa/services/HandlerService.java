package com.example.alexa.services;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

import java.util.Optional;

public interface HandlerService {
    Optional<Response> launchServices(HandlerInput input);
    Optional<Response> nameServiceHandle(HandlerInput input);

    Optional<Response> playSelectedMusic(HandlerInput input);

    Optional<Response> nextIntentHandle(HandlerInput input);

    Optional<Response> playNextIntent(HandlerInput input);

    Optional<Response> chooseTheWay(HandlerInput input);

    Optional<Response> noIntentHandler(HandlerInput input);

    Optional<Response> yesIntentHandler(HandlerInput input);

}
