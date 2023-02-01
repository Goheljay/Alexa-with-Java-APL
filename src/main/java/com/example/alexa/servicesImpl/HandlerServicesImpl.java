package com.example.alexa.servicesImpl;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.*;
import com.amazon.ask.model.interfaces.alexa.presentation.apl.RenderDocumentDirective;
import com.amazon.ask.model.ui.OutputSpeech;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.amazon.ask.response.ResponseBuilder;
import com.example.alexa.modal.ArtistEntity;
import com.example.alexa.modal.GlobalEntity;
import com.example.alexa.modal.MusicEntity;
import com.example.alexa.services.HandlerService;
import com.example.alexa.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.graalvm.compiler.core.common.util.Util;

import java.io.File;
import java.util.*;

public class HandlerServicesImpl implements HandlerService {

    public static GlobalEntity globalEntity = new GlobalEntity();

    @Override
    public Optional<Response> launchServices(HandlerInput input) {
        ResourceBundle rb = ResourceBundle.getBundle("resources");
        System.out.println("Main Card Started");
        Optional<Response> resp = input.getResponseBuilder()
                .withSimpleCard("Main Card", rb.getString("welcome"))
                .withSpeech(rb.getString("welcome"))
                .withShouldEndSession(false)
                .build();
        return  resp;
    }

    @Override
    public Optional<Response> nameServiceHandle(HandlerInput input) {
        Request request = input.getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
        Slot name = slots.get("name");
        globalEntity.setName(name.getValue());
        ResourceBundle rb = ResourceBundle.getBundle("resources");
        String speech = Utils.convertToSsml(globalEntity.getName(), rb.getString("usernameResp"));
        ResponseBuilder builder = new ResponseBuilder();
        Optional<Response> mainCard = builder.withSimpleCard("Singer List", rb.getString("usernameResp").replace("#", globalEntity.getName()).replace("@", " ")).withSpeech(speech).withShouldEndSession(false).build();
        return mainCard;
    }

    @Override
    public Optional<Response> playSelectedMusic(HandlerInput input) {
        System.out.println("start play selection music");
        List<ArtistEntity> entities = new ArrayList<>();
        entities.add(new ArtistEntity("Arijit sigh", "https://demo7uyidtyietyuiet.s3.ap-south-1.amazonaws.com/30+Second+Ke+gane++Arjit+Singh+song+status+video++New+Hindi+whatsapp+%23status+Video+30%27s+songs.mp3"));
        entities.add(new ArtistEntity("Imagine Dragons", "https://demo7uyidtyietyuiet.s3.ap-south-1.amazonaws.com/Imagine+Dragons+-+Thunder.mp3"));
        entities.add(new ArtistEntity("Martin garrix", "https://demo7uyidtyietyuiet.s3.ap-south-1.amazonaws.com/Martin+Garrix+-+Animals+(Official+Video).mp3"));

        Request request = input.getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
        Slot name = slots.get("songname");
        String speech = null;
        ResponseBuilder builder = new ResponseBuilder();
        globalEntity.setArtistName(name.getValue());
        ResourceBundle rb = ResourceBundle.getBundle("resources");
        System.out.println("name of artis     " + globalEntity.getArtistName());
        if (globalEntity.getArtist()) {
            if (Objects.equals(globalEntity.getArtistName(), "Arijit Singh")) {
                return Utils.playAudio(entities.get(0), "Playing Arijit Singh", 100L, input);
            } else if (Objects.equals(globalEntity.getArtistName(), "martin garrix")) {
                return Utils.playAudio(entities.get(1), "Playing Martin garrix", 100L, input);
            } else if (Objects.equals(globalEntity.getArtistName(), "imagine dragons")) {
                return Utils.playAudio(entities.get(2), "Playing Imagine Dragons", 100L, input);
            } else {
                speech = Utils.convertToSsml(rb.getString("notFoundArtist"));
            }
        }
        return builder.withSimpleCard("Artist Not Found", rb.getString("notFoundArtist").replace("@", " ")).withSpeech(speech).withShouldEndSession(false).build();
    }

    @Override
    public Optional<Response> nextIntentHandle(HandlerInput input) {
        System.out.println("Next Intent Call...");
        ResourceBundle rb = ResourceBundle.getBundle("resources");
        String likeSong = rb.getString("likeSong");
        return input.getResponseBuilder().withSpeech(likeSong)
                .withSimpleCard("Like or  Not", likeSong).withShouldEndSession(false).build();
    }

    @Override
    public Optional<Response> playNextIntent(HandlerInput input) {
        System.out.println("inPlaybackNearlyFinishedRequestHandler");
        String token = input.getRequestEnvelope().getContext().getAudioPlayer().getToken();
        System.out.println("Token ::::" + token);
        if (token.equals("AudioEducate")) {
            System.out.println("inPlaybackNearlyFinishedRequestHandler--> token match");
            return Utils.playNextAudio(input);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Response> chooseTheWay(HandlerInput input) {
        List<ArtistEntity> entities = new ArrayList<>();
        entities.add(new ArtistEntity("Arijit sigh", "https://demo7uyidtyietyuiet.s3.ap-south-1.amazonaws.com/30+Second+Ke+gane++Arjit+Singh+song+status+video++New+Hindi+whatsapp+%23status+Video+30%27s+songs.mp3"));
        entities.add(new ArtistEntity("Imagine Dragons", "https://demo7uyidtyietyuiet.s3.ap-south-1.amazonaws.com/Imagine+Dragons+-+Thunder.mp3"));
        entities.add(new ArtistEntity("Martin garrix", "https://demo7uyidtyietyuiet.s3.ap-south-1.amazonaws.com/Martin+Garrix+-+Animals+(Official+Video).mp3"));
        entities.add((new ArtistEntity("Video Play","https://audioeducate.com/wp-content/uploads/2021/08/SampleVideo_1280x720_1mb.mp4")));
        System.out.println("Start Choose Music");
        Request request = input.getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
        Slot way = slots.get("way");
        System.out.println("slots:::::" + way.toString());
        System.out.println("way ::::::;" + way.getValue());
        ResourceBundle rb = ResourceBundle.getBundle("resources");
        globalEntity.setChooseWay(way.getValue());
        if (Objects.equals(globalEntity.getChooseWay(), "artist")) {
            System.out.println("Selected Artist");
            globalEntity.setArtist(true);
            String artist = Utils.convertToSsml(rb.getString("artistResp"));
            return input.getResponseBuilder()
                    .withCard(Utils.simpleCard(rb.getString("artistResp").replace("@", " "), "Select Artist"))
                    .withSpeech(artist).build();
        } else if (Objects.equals(globalEntity.getChooseWay(), "genere")) {
            System.out.println("selected Genere");
            globalEntity.setGenere(true);
            String genereResponse = rb.getString("genereResponse");
            return Utils.playAudio(entities.get(3),"Video Playing",100L,input);
        } else if (Objects.equals(globalEntity.getChooseWay(), "keyword")) {
            System.out.println("selected Keyword");
            globalEntity.setKeyword(true);
            String typeToPlay = rb.getString("typeToPlay");
            return input.getResponseBuilder().withSpeech(typeToPlay)
                    .withCard(Utils.simpleCard(typeToPlay, "Say Artist Name"))
                    .withShouldEndSession(false).build();
        } else {
            return input.getResponseBuilder().withSpeech(Utils.convertToSsml(rb.getString("notFoundWay")))
                    .withSimpleCard("Not Found", rb.getString("notFoundWay")
                            .replace("@", " ")).build();
        }
    }

    @Override
    public Optional<Response> noIntentHandler(HandlerInput input) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("resources");
        String noReviewResponse = resourceBundle.getString("noReviewResponse");
        return input.getResponseBuilder().withSpeech(noReviewResponse).withSimpleCard("Not Like",noReviewResponse).withShouldEndSession(true).build();    }

    @Override
    public Optional<Response> yesIntentHandler(HandlerInput input) {
        ResourceBundle rb = ResourceBundle.getBundle("resources");
        String yesReviewResponse = rb.getString("yesReviewResponse");
        return input.getResponseBuilder().withSimpleCard("Like", yesReviewResponse).withSpeech(yesReviewResponse).withShouldEndSession(true).build();
    }

}
