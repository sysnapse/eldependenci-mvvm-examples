package com.ericlam.mc.mvvm.examples.multichat;

import org.bukkit.Material;
import org.eldependenci.mvvm.model.PropValue;
import org.eldependenci.mvvm.model.StateValue;
import org.eldependenci.mvvm.view.RenderView;
import org.eldependenci.mvvm.view.UIContext;
import org.eldependenci.mvvm.view.View;
import org.eldependenci.mvvm.view.ViewDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ViewDescriptor(
        title = "Multi Chat",
        patterns = {
                "BCCAAACCB",
                "BCCAAACCB",
                "BCCAAACCB",
                "BCCAAACCB",
                "BBBBFBBBB",
                "BBBDBEBBB"
        }
)
public class ChatView implements View {

    @RenderView('B')
    public void renderDecorate(UIContext context) {
        var btn = context.createButton();
        context.fill(
                btn.decorate(f -> f.material(Material.BLACK_STAINED_GLASS_PANE))
                        .create()
        );
    }

    @RenderView('F')
    public void renderUser(UIContext context,
                           @PropValue("mvvm.user") String user,
                           @PropValue("roomId") String id,
                           @StateValue("participants") List<String> participants){
        var btn = context.createButton();
        List<String> lore = new ArrayList<>();
        lore.add("&eRoomId: &f"+id);
        lore.add("&eParticipants: ");
        lore.addAll(participants.stream().map(p -> "&a - "+p).toList());
        context.add(
                btn.decorate(f -> f.display("&bHello, "+user).lore(lore)).create()
        );
    }

    @RenderView('D')
    public void renderInputButton(UIContext context, @StateValue("inputs") List<String> inputMessage){
        var btn = context.createButton();

        var inputs = new ArrayList<>(inputMessage.stream().map(s -> "&f" + s).toList());
        inputs.add("&bLeft click to append input messages");
        inputs.add("&cRight click to remove last line of input messages");

        context.fill(
                btn.decorate(f -> f.display("&eInput Message: ").lore(inputs))
                        .create()
        );
    }

    @RenderView('E')
    public void renderClickButton(UIContext context){
        var btn = context.createButton();
        context.add(
                btn.decorate(f -> f.display("&eSend").lore("&7Click to send message")).create()
        );
    }

    @RenderView('C')
    public void renderChatList(UIContext context, @StateValue("chat-list") Map<String, String> chatList, @PropValue("user") String user){
        var btn = context.createButton();
        int maxLine = 4;

        int index = 0;
        for(String player : chatList.keySet()){
            var chat = Arrays.stream(chatList.get(player).split("\n")).map(c -> "&e"+c).toList();
            final int i = index;

            Runnable chatPlacement = () -> context.add(
                    btn.decorate(f -> f.material(Material.PLAYER_HEAD).display("&f"+player)).create(),
                    btn.decorate(f -> f.material(i%2 == 0 ? Material.WHITE_WOOL : Material.GREEN_WOOL).display("&bSays: ").lore(chat)).create()
            );

            Runnable airPlacement = () -> context.add(
                    btn.decorate(f -> f.material(Material.AIR)).create(),
                    btn.decorate(f -> f.material(Material.AIR)).create()
            );

            if (player.equalsIgnoreCase(user)){
                airPlacement.run();
                chatPlacement.run();
            }else{
                chatPlacement.run();
                airPlacement.run();
            }

            index++;

            if (index == maxLine){
                break;
            }

        }
    }




}
