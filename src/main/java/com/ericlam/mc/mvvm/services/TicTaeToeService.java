package com.ericlam.mc.mvvm.services;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public sealed interface TicTaeToeService permits TicTaeToeServiceImpl {

    GameBuilder build(Player player);

    void quitGame(Player player);

    interface GameBuilder {

        GameBuilder onGameStart(Consumer<Player> gameStart);

        GameBuilder onMovementComplete(Consumer<String> completer);

        GameBuilder onGameEnd(Consumer<String> gameEnd);

        Game waitingForTarget();
    }

    interface Game {

        void finishRound(int x, int y);

        String[][] getCurrentBoard();

    }

}
