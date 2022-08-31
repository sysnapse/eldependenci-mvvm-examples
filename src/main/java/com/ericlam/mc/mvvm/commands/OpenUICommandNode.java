package com.ericlam.mc.mvvm.commands;

import com.ericlam.mc.eld.annotations.RemainArgs;
import com.ericlam.mc.eld.bukkit.CommandNode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldependenci.mvvm.InventoryService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

public abstract class OpenUICommandNode implements CommandNode {

    @Inject
    protected InventoryService service;
    @RemainArgs
    private List<String> args;

    @Override
    public void execute(CommandSender commandSender) {
        if (!(commandSender instanceof Player player)){
            commandSender.sendMessage("&cPlayer only");
            return;
        }

        var action = this.getClass().getAnnotation(OpenUIAction.class);
        if (action == null){
            this.openUI(player, args);
        }else{
            var v = action.view();
            var props = action.props();
            if (props.length > args.size()){
                player.sendMessage("args not enough: "+String.join(", ", props));
                return;
            }
            var propsInput = new HashMap<String, Object>();
            for (int i = 0; i < props.length; i++) {
                var key = props[i];
                var value = args.get(i);
                propsInput.put(key, value);
            }
            service.openUI(player, v, propsInput);
        }
    }


    protected void openUI(Player player, List<String> args){
        throw new IllegalStateException("no openUI action implemented and no @OpenUIAction annotated");
    }
}
