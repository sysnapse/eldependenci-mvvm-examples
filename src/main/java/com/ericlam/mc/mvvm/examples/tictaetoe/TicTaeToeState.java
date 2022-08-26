package com.ericlam.mc.mvvm.examples.tictaetoe;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.eldependenci.mvvm.model.StateHolder;

public interface TicTaeToeState extends StateHolder {

    Player getTarget();

    void setTarget(Player target);

    String[][] getBoard();

    void setBoard(String[][] board);

    GameState getGameState();

    void setGameState(GameState result);

    enum GameState {
        WIN("&6You Won!", Material.BEACON),
        LOSE("&cYou Lose...", Material.DAMAGED_ANVIL),
        TARGET_TURN("&eTarget Turn", Material.DRAGON_HEAD),
        PLAYER_TURN("&bYour Turn", Material.PLAYER_HEAD),
        WAITING_TARGET("&9Waiting For Target..", Material.DIAMOND_PICKAXE),

        GAME_DRAW("&6Game Draw", Material.SPRUCE_WOOD);

        public final String title;
        public final Material icon;

        GameState(String title, Material icon) {
            this.title = title;
            this.icon = icon;
        }
    }
}
