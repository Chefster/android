package com.codepath.chefster.utils;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;

public class VoiceRecognitionListener implements RecognitionListener {

    private static VoiceRecognitionListener instance = null;

    IVoiceControl listener; // This is your running activity (we will initialize it later)

    public static VoiceRecognitionListener getInstance() {
        if (instance == null) {
            instance = new VoiceRecognitionListener();
        }
        return instance;
    }

    private VoiceRecognitionListener() { }

    public void setListener(IVoiceControl listener) {
        this.listener = listener;
    }

    public void processVoiceCommands(String... voiceCommands) {
        listener.processVoiceCommands(voiceCommands);
    }

    // This method will be executed when voice commands were found
    public void onResults(Bundle data) {
        ArrayList<String> matches = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String[] commands = new String[matches.size()];
        for (String command : matches) {
            System.out.println(command);
        }
        commands = matches.toArray(commands);
        processVoiceCommands(commands);
    }

    // User starts speaking
    public void onBeginningOfSpeech() {
        System.out.println("Starting to listen");
    }

    public void onBufferReceived(byte[] buffer) { }

    // User finished speaking
    public void onEndOfSpeech() {
        System.out.println("Waiting for result...");
    }

    // If the user said nothing the service will be restarted
    public void onError(int error) {
        if (listener != null) {
            listener.restartListeningService();
        }
    }
    public void onEvent(int eventType, Bundle params) { }

    public void onPartialResults(Bundle partialResults) { }

    public void onReadyForSpeech(Bundle params) { }

    public void onRmsChanged(float rmsdB) { }
}