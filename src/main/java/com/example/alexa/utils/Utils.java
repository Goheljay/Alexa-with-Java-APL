package com.example.alexa.utils;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Directive;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.alexa.presentation.apl.*;
import com.amazon.ask.model.interfaces.audioplayer.AudioItem;
import com.amazon.ask.model.interfaces.audioplayer.PlayBehavior;
import com.amazon.ask.model.interfaces.audioplayer.PlayDirective;
import com.amazon.ask.model.interfaces.audioplayer.Stream;
import com.amazon.ask.model.ui.OutputSpeech;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.example.alexa.modal.ArtistEntity;
import com.example.alexa.modal.MusicEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Utils {

    public static String convertToSsml(String name, String speech) {
        String nameSpeech = speech.replace("#", name);
        String generatedSpeech = nameSpeech.replace("@","<break time= '0.5s' strength='weak'/>");
        return generatedSpeech;
    }
    public static String convertToSsml(String speech) {
        String generatedSpeech = speech.replace("@","<break time= '0.5s' strength='weak'/>");
        return generatedSpeech;
    }
    public static SimpleCard simpleCard (String speechText, String cardTitle) {
        return SimpleCard.builder().withContent(speechText).withTitle(cardTitle).build();
    }

    public static Optional<Response> playAudio(ArtistEntity entity, String speechText, Long offSet, HandlerInput input) {
        // Create the ssml output
        SimpleCard card = Utils.simpleCard(speechText, "Simple Card");
        OutputSpeech outputSpeech = SsmlOutputSpeech.builder()
                .withSsml("<speak> <p> <s> " + speechText.replace("!", " ") + "</s> </p> </speak>").build();
        // Create a stream for the audio link
        Stream stream = Stream.builder().withUrl(entity.getSongList()).withToken("AudioEducate").withOffsetInMilliseconds(offSet)
                .build();

        // Device is already inserted
        String[] splitUrl = entity.getSongList().split("\\.");
        String extension = splitUrl[((splitUrl.length) - 1)];
        if (extension.toLowerCase().equals("mp3")) {
            // set the audio stream to AudioItem

            AudioItem audioItem = AudioItem.builder().withStream(stream).build();
            System.out.println("===Audio File Playing===");
            // Create a play Directive
            PlayDirective playDirective = PlayDirective.builder().withAudioItem(audioItem)
                    .withPlayBehavior(PlayBehavior.REPLACE_ALL).build();
            // Add the library to the list
            List<Directive> dirList = new ArrayList<>();
            dirList.add(playDirective);
            System.out.println("======================== End ==============================");
            return Optional.of(Response.builder().withOutputSpeech(outputSpeech).withDirectives(dirList)
                            .withCard(card)
                    .withShouldEndSession(false).build());
        }
        else {
            if (input.getRequestEnvelope().getContext().getSystem().getDevice().getSupportedInterfaces()
                    .getAlexaPresentationAPL() != null || input.getRequestEnvelope().getContext().getSystem().getDevice().getSupportedInterfaces()
                    .getVideoApp() != null) {
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<HashMap<String, Object>> documentMapType = new TypeReference<HashMap<String, Object>>() {
                };
                ObjectMapper dataMapper = new ObjectMapper();
                TypeReference<HashMap<String, Object>> dataMapType = new TypeReference<HashMap<String, Object>>() {
                };

                //ObjectMapper dmapper = new ObjectMapper();
                ObjectNode rootNode = dataMapper.createObjectNode();
                ObjectNode childNode1 = dataMapper.createObjectNode();
                childNode1.put("primaryText", "Hello World!");
                childNode1.put("secondaryText", "Welcome to Alexa Presentation Language!");
                rootNode.set("helloWorldDataSource", childNode1);
                ObjectNode childNode2 = dataMapper.createObjectNode();
                childNode2.put("description", "video 1");
                childNode2.put("duration", 0);
                childNode2.put("offset", 0);
                childNode2.put("repeatCount", 0);
                childNode2.put("url", entity.getSongList());
                rootNode.set("track1", childNode2);
                /*
                 * ObjectNode rootchildNode3 = dataMapper.createObjectNode();
                 * ObjectNode childNode3 = dataMapper.createObjectNode();
                 * childNode3.put("contentType","SSML");
                 * childNode3.put("textToSpeak", outputSpeech);
                 * rootchildNode3.set("welcomeText", childNode3);
                 * rootNode.set("helloWorldData",rootchildNode3);
                 */



                String jsonString = null;
                try {
                    jsonString = dataMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
                } catch (JsonProcessingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Map<String, Object> document = null;
                Map<String, Object> datasources = null;
                try {
                    document = mapper.readValue(
                            new File("Apl.json"),
                            documentMapType);
                    datasources = dataMapper.readValue(jsonString,
                            dataMapType);
                    // datasources =  dataMapper.readValue(new File("helloWorldDataSource.json"),
                    //          dataMapType);
                } catch (JsonParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(datasources);
                VideoSource videoSource = VideoSource.builder().withUrl(entity.getSongList()).build();
                VideoSource vs2 = VideoSource.builder().withUrl("https://learndash-material-lms.s3.amazonaws.com/alexaNext/New+Alexa+Video+Ending.mp4").build();
                List<VideoSource> source = new ArrayList<>();
                //source.set(0, videoSource);
                source.add(videoSource);
                source.add(vs2);
                AudioTrack audioTrack = AudioTrack.FOREGROUND;
                PlayMediaCommand playMediaItemCommand = PlayMediaCommand.builder()
                        .withComponentId("myAudioPlayer")
                        .withSource(source).withAudioTrack(audioTrack)
                        .build();
                MediaCommandType command = MediaCommandType.PLAY;
                ControlMediaCommand controlMediaItemCommand = ControlMediaCommand.builder()
                        .withCommand(command).withComponentId("myAudioPlayer").build();
                ExecuteCommandsDirective executeCommandsDirective = ExecuteCommandsDirective.builder()
                        .withToken("AudioEudcate")
                        .addCommandsItem(playMediaItemCommand).addCommandsItem(controlMediaItemCommand)
                        .build();
                RenderDocumentDirective renderDocumentDirective = RenderDocumentDirective.builder()
                        .withToken("AudioEudcate")
                        .withDocument(document).withDatasources(datasources)
                        .build();

                return Optional.of(Response.builder().withOutputSpeech(outputSpeech)
                                .withCard(card)
                        .addDirectivesItem(renderDocumentDirective).addDirectivesItem(executeCommandsDirective).build());

            } else {
                System.out.println("==Supported Interface =="+input.getRequestEnvelope().getContext().getSystem().getDevice().getSupportedInterfaces());
                System.out.println("==Video Audio Playing ==");
                // Create a stream for the audio link
                AudioItem audioItem = AudioItem.builder().withStream(stream).build();
                PlayDirective playDirective = PlayDirective.builder().withAudioItem(audioItem)
                        .withPlayBehavior(PlayBehavior.REPLACE_ALL).build();
                List<Directive> dirList = new ArrayList<>();
                dirList.add(playDirective);
                System.out.println("954======================== End ==============================");
                return Optional.of(Response.builder().withOutputSpeech(outputSpeech).withCard(card)
                        .withDirectives(dirList).withShouldEndSession(false).build());
            }

        }
    }
    public static Optional<Response> playbyGenere(MusicEntity musicEntity, String speechText, Long offSet, HandlerInput input) {
        SimpleCard card = Utils.simpleCard(speechText, "Simple Card");
        OutputSpeech outputSpeech = SsmlOutputSpeech.builder()
                .withSsml("<speak> <p> <s> " + speechText.replace("!", " ") + "</s> </p> </speak>").build();
        // Create a stream for the audio link
        Stream stream = Stream.builder().withUrl(musicEntity.getAudioLink()).withToken("AudioEducate").withOffsetInMilliseconds(offSet)
                .build();
        AudioItem audioItem = AudioItem.builder().withStream(stream).build();
        System.out.println("===Audio File Playing===");
        // Create a play Directive
        PlayDirective playDirective = PlayDirective.builder().withAudioItem(audioItem)
                .withPlayBehavior(PlayBehavior.REPLACE_ALL).build();

        // Add the library to the list
        List<Directive> dirList = new ArrayList<>();
        dirList.add(playDirective);
        System.out.println("======================== End ==============================");
        return Optional.of(Response.builder().withOutputSpeech(outputSpeech).withDirectives(dirList)
                .withCard(card)
                .withShouldEndSession(false).build());

    }
    public static Optional<Response> playNextAudio(HandlerInput input) {
        System.out.println("inPlayNextAudio");

        String url = "https://learndash-material-lms.s3.amazonaws.com/alexaNext/alexa-next.mp3";
        // Create a stream for the audio link

        Stream stream = Stream.builder().withUrl(url).withExpectedPreviousToken("AudioEducate")
                .withToken("NextAudio").withOffsetInMilliseconds(0L)
                .build();

        System.out.println("URL ========   " + url);

        AudioItem audioItem = AudioItem.builder().withStream(stream).build();
        PlayDirective playDirective = PlayDirective.builder().withAudioItem(audioItem)
                .withPlayBehavior(PlayBehavior.ENQUEUE).build();
        List<Directive> dirList = new ArrayList<>();
        dirList.add(playDirective);
        System.out.println("Token...."+stream.getToken());
        System.out.println("outPlayNextAudio");
        return Optional.of(Response.builder().withDirectives(dirList).build());
    }

    public static int randomNumberGenerator(int min, int max) {
        int num = (int) (Math.random() * (1 - (max == 0 ? min : max)));
        if (num == 0 || num < 0) {
            num = min;
        }
        return num;
    }
}
