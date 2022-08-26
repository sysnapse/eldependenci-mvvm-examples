package com.ericlam.mc.mvvm.services;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public non-sealed class MultiChatServiceImpl implements MultiChatService {

    private final Map<String, Map<String, MessageListener>> messageBus = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Consumer<String>>> participantBus = new ConcurrentHashMap<>();

    private final Map<String, ChatRoomList> chatRooms = new ConcurrentHashMap<>();

    @Override
    public void subscribeNewMessage(String roomId, String user, MessageListener listener) {
        messageBus.putIfAbsent(user, new ConcurrentHashMap<>());
        messageBus.get(user).put(roomId, listener);
    }

    @Override
    public void subscribeNewParticipant(String roomId, String user, Consumer<String> participant) {
        participantBus.putIfAbsent(user, new ConcurrentHashMap<>());
        participantBus.get(user).put(roomId, participant);
    }

    @Override
    public void pushMessage(String roomId, String user, List<String> messages) {
        chatRooms.putIfAbsent(roomId, new ChatRoomList());
        var chatRoom = chatRooms.get(roomId);
        var message = String.join("\n", messages);
        chatRoom.chats.add(new ChatLine(user, message));
        if (!chatRoom.participants.contains(user)) {
            chatRoom.participants.add(user);
            participantBus.values().stream()
                    .flatMap(m -> m.entrySet().stream())
                    .filter(en -> en.getKey().equals(roomId))
                    .map(Map.Entry::getValue)
                    .forEach(consumer -> consumer.accept(user));
        }
        messageBus.values().stream()
                .flatMap(m -> m.entrySet().stream())
                .filter(en -> en.getKey().equals(roomId))
                .map(Map.Entry::getValue)
                .forEach(listen -> listen.onMessageReceived(user, message));
    }


    @Override
    public void unsubscribeAll(String roomId, String user) {
        var msg = messageBus.get(user);
        if (msg != null) {
            msg.remove(roomId);
        }
        var par = participantBus.get(user);
        if (par != null) {
            par.remove(roomId);
        }
        if (!chatRooms.containsKey(roomId)) return;
        var room = chatRooms.get(roomId);
        room.participants.remove(user);
        if (room.participants.isEmpty()) {
            chatRooms.remove(roomId);
        }
    }

    @Override
    public List<ChatLine> getChatHistory(String roomId) {
        if (!chatRooms.containsKey(roomId)) return List.of();
        var room = chatRooms.get(roomId);
        return List.copyOf(room.chats);
    }

    @Override
    public List<String> getParticipants(String roomId) {
        if (!chatRooms.containsKey(roomId)) return List.of();
        var room = chatRooms.get(roomId);
        return ImmutableList.copyOf(room.participants);
    }

    private static class ChatRoomList {
        private final List<ChatLine> chats = new LinkedList<>();
        private final List<String> participants = new ArrayList<>();
    }

}
