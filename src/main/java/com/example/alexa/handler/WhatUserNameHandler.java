package com.example.alexa.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.example.alexa.services.HandlerService;
import com.example.alexa.servicesImpl.HandlerServicesImpl;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class WhatUserNameHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("WhatUserName"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        HandlerService service = new HandlerServicesImpl();
        Optional<Response> resp = service.nameServiceHandle(input);
        return resp;
    }
}
