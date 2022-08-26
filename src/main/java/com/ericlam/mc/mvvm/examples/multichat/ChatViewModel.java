package com.ericlam.mc.mvvm.examples.multichat;

import com.ericlam.mc.mvvm.services.ChatLine;
import com.ericlam.mc.mvvm.services.MultiChatService;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.eldependenci.mvvm.model.State;
import org.eldependenci.mvvm.viewmodel.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

@ViewModelBinding(ChatView.class)
public class ChatViewModel implements ViewModel {

    @State
    private ChatState state;
    @Context
    private ViewModelContext context;

    @Inject
    private MultiChatService chatService;

    private String user = "", id = "";

    @Override
    public void init(Player player, Map<String, Object> props) {
        user = (String) props.get("user");
        if (!player.getName().equalsIgnoreCase(user)){
            throw new IllegalStateException("username is not match as player name");
        }

        id = (String)props.get("roomId");
        if (id == null || id.isBlank()){
            throw new IllegalStateException("room id cannot be blank");
        }

        var chatHistory = chatService.getChatHistory(id);

        // take last 4 messages only
        var chatList = chatHistory.stream().skip(chatHistory.size() - 4).toList();


        state.setChatList(new LinkedList<>(chatList));
        state.setParticipants(chatService.getParticipants(id));
        state.setInputs(new ArrayList<>());

        chatService.subscribeNewMessage(id, user, (participant, message) -> {
            var list = state.getChatList();
            // if size larger than 4, remove first
            if (list.size() == 4){
                list.removeFirst();
            }
            list.add(new ChatLine(participant, message));
            state.setChatList(list);
        });

        chatService.subscribeNewParticipant(id, user, participant -> {
            state.setParticipants(chatService.getParticipants(id));
        });
    }

    @ClickMapping('D')
    public void onInputClick(InventoryClickEvent e){
        if (e.isLeftClick()){
            context.observeEvent(AsyncChatEvent.class, 200, event -> {
                var input = state.getInputs();
                input.add(((TextComponent)event.message()).content());
                state.setInputs(input);
            });
        }else if (e.isRightClick()){
            var input = state.getInputs();
            input.remove(input.size() - 1);
            state.setInputs(input);
        }
    }

    @ClickMapping('E')
    public void onClickSend(InventoryClickEvent e){
        chatService.pushMessage(id, user, state.getInputs());
        state.setInputs(new ArrayList<>()); // clear sent message
    }

    @Override
    public void beforeUnMount(Player player) {
        chatService.unsubscribeAll(id, user);
    }
}
