package com.ericlam.mc.mvvm.commands;


import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.mvvm.examples.multichat.ChatViewModel;

@Commander(name = "mutlichat", description = "multichat ui open command")
@OpenUIAction(view = ChatViewModel.class, props = {"roomId"})
public class MultiPlayerChatUI extends OpenUICommandNode{
}
