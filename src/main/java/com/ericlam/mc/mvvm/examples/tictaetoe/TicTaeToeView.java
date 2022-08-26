package com.ericlam.mc.mvvm.examples.tictaetoe;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.eldependenci.mvvm.model.PropValue;
import org.eldependenci.mvvm.model.StateValue;
import org.eldependenci.mvvm.view.*;

@ViewDescriptor(
        title = "triple cross",
        patterns = {
                "XXXXXXXXX",
                "XXXAAAXXX",
                "XWXAAAXDX",
                "XXXAAAXXX",
                "XXXXXXXXX"
        }
)
public class TicTaeToeView implements View {

    @RenderView('X')
    public void renderDecorate(UIContext context) {
        var btn = context.createButton();
        context.fill(
                btn.decorate(f -> f.material(Material.BLACK_STAINED_GLASS_PANE)).create()
        );
    }

    @RenderView('W')
    public void renderTargetInfo(UIContext context, @StateValue("target") Player target){
        var btn = context.createButton();
        UIButton button;
        if (target == null){
            button = btn.decorate(f -> f.material(Material.WHITE_WOOL).display("&e尋找對手中...")).create();
        }else{
            button = btn.decorate(f -> f.material(Material.ORANGE_WOOL).display("&b"+target.getName())).create();
        }
        context.add(button);
    }

    @RenderView('D')
    public void renderResultState(UIContext context, @StateValue("game-state") TicTaeToeState.GameState state){
        var btn = context.createButton();
        context.add(
                btn.decorate(f -> f.material(state.icon).display(state.title)).create()
        );
    }

    @RenderView('A')
    public void renderCrossPanel(UIContext context,
                                 @StateValue("board") String[][] board,
                                 @StateValue("target") Player target,
                                 @PropValue("mvvm.user") Player user
    ) {
        var btn = context.createButton();
        if (board.length > 3){
            throw new IllegalStateException("state length over than 3");
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                String operator = board[i][j];
                UIButton button;
                if (operator == null || operator.isBlank()){ // is default
                    button = btn.decorate(f -> f.material(Material.WHITE_WOOL)).create();
                }else if (operator.equals(target.getName())){
                    button = btn.decorate(f -> f.material(Material.RED_WOOL)).create();
                }else if (operator.equals(user.getName())){ // player itself
                    button = btn.decorate(f -> f.material(Material.GREEN_WOOL)).create();
                }else{
                    throw new IllegalStateException("unknown operator: "+operator);
                }
                context.set(j + 3 * i, button);
            }
        }
    }

}
