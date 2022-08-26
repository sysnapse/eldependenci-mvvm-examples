package com.ericlam.mc.mvvm.examples.multichat;

import com.ericlam.mc.mvvm.services.ChatLine;
import org.eldependenci.mvvm.model.StateHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface ChatState extends StateHolder {

    LinkedList<ChatLine> getChatList();

    void setChatList(LinkedList<ChatLine> chatList);

    List<String> getParticipants();

    void setParticipants(List<String> participants);

    List<String> getInputs();

    void setInputs(List<String> inputs);

}
