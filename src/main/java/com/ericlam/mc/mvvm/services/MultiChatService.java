package com.ericlam.mc.mvvm.services;

import java.util.List;
import java.util.function.Consumer;

public interface MultiChatService {

    void subscribeNewMessage(String roomId, String user, MessageListener listener);

    void subscribeNewParticipant(String roomId, String user, Consumer<String> participant);

    void pushMessage(String roomId, String user, List<String> messages);

    void unsubscribeAll(String roomId, String user);

    List<ChatLine> getChatHistory(String roomId);

    List<String> getParticipants(String roomId);

    @FunctionalInterface
    interface MessageListener {

        void onMessageReceived(String participant, String message);

    }
}
