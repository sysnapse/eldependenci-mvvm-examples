package com.ericlam.mc.mvvm.commands;

import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.annotations.RemainArgs;
import com.ericlam.mc.eld.bukkit.CommandNode;
import org.bukkit.command.CommandSender;
import org.eldependenci.mvvm.InventoryService;

import javax.inject.Inject;
import java.util.List;

@Commander(
        name = "mvvm-examples",
        description = "mvvm example main command",
        alias = {"mvvmexam", "mvvmdemo", "mvvmexample"}
)
public class ExamplesCommand implements CommandNode {

    @Override
    public void execute(CommandSender commandSender) {
    }
}
