package com.example.alexa.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.example.alexa.services.HandlerService;
import com.example.alexa.servicesImpl.HandlerServicesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.amazon.ask.request.Predicates.requestType;
import java.util.Optional;

public class LaunchHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        HandlerService service = new HandlerServicesImpl();
        Optional<Response> launchResp = service.launchServices(input);
        return launchResp;
    }
}
