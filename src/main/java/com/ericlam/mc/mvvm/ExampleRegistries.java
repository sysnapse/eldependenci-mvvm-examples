package com.ericlam.mc.mvvm;

import com.ericlam.mc.eld.bukkit.CommandNode;
import com.ericlam.mc.eld.bukkit.ComponentsRegistry;
import com.ericlam.mc.eld.registration.CommandRegistry;
import com.ericlam.mc.eld.registration.ListenerRegistry;
import org.bukkit.event.Listener;

public final class ExampleRegistries implements ComponentsRegistry {
    @Override
    public void registerCommand(CommandRegistry<CommandNode> commandRegistry) {

    }

    @Override
    public void registerListeners(ListenerRegistry<Listener> listenerRegistry) {

    }
}
