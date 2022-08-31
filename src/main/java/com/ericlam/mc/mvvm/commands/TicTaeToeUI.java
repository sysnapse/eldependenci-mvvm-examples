package com.ericlam.mc.mvvm.commands;


import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.mvvm.examples.tictaetoe.TicTaeToeViewModel;

@Commander(name = "tictaetoe", description = "tictaetoe ui command")
@OpenUIAction(view = TicTaeToeViewModel.class)
public class TicTaeToeUI extends OpenUICommandNode{
}
