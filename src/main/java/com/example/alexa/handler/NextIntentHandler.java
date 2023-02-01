package com.example.alexa.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.example.alexa.services.HandlerService;
import com.example.alexa.servicesImpl.HandlerServicesImpl;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

public class NextIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.NextIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        System.out.println("Next Intent.........."+input.toString());
        HandlerService service = new HandlerServicesImpl();
        Optional<Response> resp = service.nextIntentHandle(input);
        System.out.println("Next Intent Close...");
        return resp;
    }
}
