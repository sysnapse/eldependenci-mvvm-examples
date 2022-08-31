package com.ericlam.mc.mvvm;

import com.ericlam.mc.eld.bukkit.CommandNode;
import com.ericlam.mc.eld.bukkit.ComponentsRegistry;
import com.ericlam.mc.eld.registration.CommandRegistry;
import com.ericlam.mc.eld.registration.ListenerRegistry;
import com.ericlam.mc.mvvm.commands.ExamplesCommand;
import com.ericlam.mc.mvvm.commands.MultiPlayerChatUI;
import com.ericlam.mc.mvvm.commands.TicTaeToeUI;
import org.bukkit.event.Listener;

public final class ExampleRegistries implements ComponentsRegistry {
    @Override
    public void registerCommand(CommandRegistry<CommandNode> commandRegistry) {
        commandRegistry.command(ExamplesCommand.class, c -> {
            c.command(TicTaeToeUI.class);
            c.command(MultiPlayerChatUI.class);
        });
    }

    @Override
    public void registerListeners(ListenerRegistry<Listener> listenerRegistry) {

    }
}
