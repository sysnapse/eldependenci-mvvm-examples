package com.ericlam.mc.mvvm.services;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class TicTaeToeServiceImpl implements TicTaeToeService {

    private static final int[][][] POSSIBLE_WINS = new int[][][]{
            {{0, 0}, {0, 1}, {0, 2}},
            {{1, 0}, {1, 1}, {1, 2}},
            {{2, 0}, {2, 1}, {2, 2}},
            {{0, 0}, {1, 0}, {2, 0}},
            {{0, 1}, {1, 1}, {2, 1}},
            {{0, 2}, {1, 2}, {2, 2}},
            {{0, 0}, {1, 1}, {2, 2}},
            {{0, 2}, {1, 1}, {2, 0}}
    };

    private final Map<Player, P2PGame> gameMap = new ConcurrentHashMap<>();
    private final Queue<GameImpl> gameQueue = new ConcurrentLinkedQueue<>();

    private void checkQueue() {
        // clean not online
        gameQueue.removeIf(g -> !g.player.isOnline());
        if (gameQueue.size() >= 2) {

            var gamer1 = gameQueue.poll();
            var gamer2 = gameQueue.poll();

            P2PGame game = new P2PGame(gamer1, gamer2);
            game.start();

            this.gameMap.put(gamer1.player, game);
            this.gameMap.put(gamer2.player, game);
        }
    }


    @Override
    public GameBuilder build(Player player) {
        return new GameBuilderImpl(player);
    }

    @Override
    public void quitGame(Player player) {
        gameQueue.removeIf(g -> g.player == player);
        P2PGame game = gameMap.get(player);
        if (game == null) return;
        game.quit(player);
    }

    private class GameBuilderImpl implements GameBuilder {

        private final Player player;

        private Consumer<Player> gameStart;
        private Consumer<String> movementComplete;
        private Consumer<String> gameEnd;

        private GameBuilderImpl(Player player) {
            this.player = player;
        }

        @Override
        public GameBuilder onGameStart(Consumer<Player> gameStart) {
            this.gameStart = gameStart;
            return this;
        }

        @Override
        public GameBuilder onMovementComplete(Consumer<String> completer) {
            this.movementComplete = completer;
            return this;
        }


        @Override
        public GameBuilder onGameEnd(Consumer<String> gameEnd) {
            this.gameEnd = winner -> {
                gameEnd.accept(winner);
                gameMap.remove(player);
            };
            return this;
        }

        @Override
        public Game waitingForTarget() {
            Objects.requireNonNull(this.gameEnd, "gameEnd handler is required");
            Objects.requireNonNull(this.gameStart, "gameStart handler is required");
            Objects.requireNonNull(this.movementComplete, "movement complete handler is required");
            GameImpl g = new GameImpl(player, gameStart, movementComplete, gameEnd);
            gameQueue.offer(g);
            checkQueue();
            return g;
        }
    }

    private static final class P2PGame implements GameController {

        private final GameImpl playerA;
        private final GameImpl playerB;

        private final String[][] board = new String[3][3];

        private P2PGame(GameImpl playerA, GameImpl playerB) {
            this.playerA = playerA;
            this.playerB = playerB;

            this.playerA.setController(this);
            this.playerB.setController(this);
        }

        void start() {
            playerA.gameStart.accept(playerB.player);
            playerB.gameStart.accept(playerA.player);
            // random first
            var starter = Math.random() > 0.5 ? playerA : playerB;
            playerA.movementComplete.accept(starter.player.getName());
            playerB.movementComplete.accept(starter.player.getName());
        }

        void quit(Player player) {
            if (player == playerA.player) {
                playerB.gameEnd.accept(playerB.player.getName());
            } else if (player == playerB.player) {
                playerA.gameEnd.accept(playerA.player.getName());
            } else {
                throw new IllegalStateException("unknown player quit: " + player.getName());
            }
        }

        @Override
        public void updateBoard(String operator, int x, int y) {
            board[x][y] = operator;
            String winner = getWinner();
            if (winner != null) {
                // no winner, game draw
                if (winner.isBlank()) {
                    playerA.gameEnd.accept(null);
                    playerB.gameStart.accept(null);
                } else {
                    playerA.gameEnd.accept(winner);
                    playerB.gameEnd.accept(winner);
                }
            } else {
                playerA.movementComplete.accept(operator);
                playerB.movementComplete.accept(operator);
            }
        }

        @Override
        public String[][] getBoard() {
            return Arrays.copyOf(board, board.length);
        }

        private String getWinner() {

            boolean empty = false;

            possibleWin:
            for (int[][] possibleWin : POSSIBLE_WINS) {
                Set<String> marks = new HashSet<>();
                for (int[] pos : possibleWin) {
                    String mark = board[pos[0]][pos[1]];
                    if (mark == null || mark.isBlank()){
                        empty = true;
                        continue possibleWin;
                    }
                    marks.add(mark);
                }
                if (marks.size() == 1){
                    return marks.iterator().next();
                }
            }

            return empty ? null : "";
        }
    }

    private static class GameImpl implements Game {
        private final Consumer<Player> gameStart;
        private final Consumer<String> movementComplete;
        private final Consumer<String> gameEnd;
        private final Player player;

        private GameController controller;

        private GameImpl(Player player, Consumer<Player> gameStart, Consumer<String> movementComplete, Consumer<String> gameEnd) {
            this.player = player;
            this.gameStart = gameStart;
            this.movementComplete = movementComplete;
            this.gameEnd = gameEnd;
        }

        public void setController(GameController controller) {
            this.controller = controller;
        }

        @Override
        public void finishRound(int x, int y) {
            if (controller == null) throw new IllegalStateException("Game Not Started Yet");
            controller.updateBoard(player.getName(), x, y);
        }

        @Override
        public String[][] getCurrentBoard() {
            if (controller == null) throw new IllegalStateException("Game Not Started Yet");
            return controller.getBoard();
        }
    }

    private interface GameController {

        void updateBoard(String operator, int x, int y);

        String[][] getBoard();

    }
}
