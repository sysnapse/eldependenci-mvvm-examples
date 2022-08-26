package com.ericlam.mc.mvvm.examples.tictaetoe;

import com.ericlam.mc.mvvm.services.TicTaeToeService;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.eldependenci.mvvm.model.State;
import org.eldependenci.mvvm.view.RenderView;
import org.eldependenci.mvvm.viewmodel.Context;
import org.eldependenci.mvvm.viewmodel.ViewModel;
import org.eldependenci.mvvm.viewmodel.ViewModelBinding;
import org.eldependenci.mvvm.viewmodel.ViewModelContext;

import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;

@ViewModelBinding(TicTaeToeView.class)
public class TicTaeToeViewModel implements ViewModel {

    @State
    private TicTaeToeState state;

    @Context
    private ViewModelContext context;

    @Inject
    private TicTaeToeService ticTaeToeService;

    private TicTaeToeService.Game game;

    @Override
    public void init(Player player, Map<String, Object> props) {
        game = ticTaeToeService.build(player)
                .onGameStart(target -> state.setTarget(target))
                .onMovementComplete(operator -> {

                    // update from the latest board
                    state.setBoard(game.getCurrentBoard());

                    if (operator.equals(player.getName())){
                        state.setGameState(TicTaeToeState.GameState.TARGET_TURN);
                    }else{
                        state.setGameState(TicTaeToeState.GameState.PLAYER_TURN);
                    }

                })
                .onGameEnd(winner -> {
                    if (winner == null){
                        state.setGameState(TicTaeToeState.GameState.GAME_DRAW);
                    }else{
                        state.setGameState(winner.equals(player.getName()) ? TicTaeToeState.GameState.WIN : TicTaeToeState.GameState.LOSE);
                    }
                })
                .waitingForTarget();

        state.setGameState(TicTaeToeState.GameState.WAITING_TARGET);
        state.setBoard(new String[3][3]);
    }

    @RenderView('A')
    public void onClickBoard(InventoryClickEvent e){
        if (state.getGameState() != TicTaeToeState.GameState.PLAYER_TURN){
            return;
        }

        var clicked = e.getCurrentItem();

        var anyClicked = context.getItemMap('A').entrySet().stream().filter(item -> Objects.equals(item.getValue(), clicked)).findAny();

        if (anyClicked.isEmpty()) return;

        var clickedSlot = anyClicked.get().getKey();

        int x = 0, y = 0;
        while (clickedSlot > 3){
            clickedSlot -= 3;
            x++;
        }
        y = clickedSlot;

        game.finishRound(x, y);
    }
}
