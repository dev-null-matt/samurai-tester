package samurai.tester.samurai.strategy

import samurai.tester.action.ArtfullyStrikeAction
import samurai.tester.samurai.Samurai
import samurai.tester.samurai.Strategy

import static samurai.tester.action.Action.ActionType
import static samurai.tester.action.Action.ActionType.ARTFULLY_STRIKE
import static samurai.tester.action.Action.ActionType.CATCH_YOUR_BREATH
import static samurai.tester.action.Action.ActionType.DEFTLY_COUNTER
import static samurai.tester.action.Action.ActionType.MONOPOLIZE_OPENING

class CounterAndMonopolizeStrategy extends Strategy {

    @Override
    ActionType getAction(Samurai attacker, Samurai defender) {
        return defender.flaws.isEmpty() ? attacker.harmBoxes == attacker.endurance + 4 ? ARTFULLY_STRIKE : CATCH_YOUR_BREATH : MONOPOLIZE_OPENING
    }

    @Override
    ActionType getReaction(Samurai attacker, Samurai defender, ActionType actionType, List<Integer> actionOptions) {
        return DEFTLY_COUNTER
    }

    @Override
    boolean shouldInvokeFlaw(Samurai defender, ActionType actionType) {
        return defender.flaws.size() > 1
    }

    @Override
    List<Integer> pickOptions(Samurai attacker, Samurai defender, ActionType actionType, int optionCount) {

        switch (actionType) {
            case CATCH_YOUR_BREATH:
                return attacker.flaws.contains(ArtfullyStrikeAction.OFF_BALANCE) ? [0,3,1,2] : [0,2,3,1]
            case DEFTLY_COUNTER:
                return [0,3,1,2]
            case MONOPOLIZE_OPENING:
                return [0,1,2,3]
            case ARTFULLY_STRIKE:
                return [0,1,2,3]
        }
    }
}
