package samurai.tester.samurai.strategy

import samurai.tester.action.ArtfullyStrikeAction
import samurai.tester.samurai.Samurai
import samurai.tester.samurai.Strategy

import static samurai.tester.action.Action.ActionType
import static samurai.tester.action.Action.ActionType.ARTFULLY_STRIKE
import static samurai.tester.action.Action.ActionType.CATCH_YOUR_BREATH
import static samurai.tester.action.Action.ActionType.DEFTLY_COUNTER

class ChainCounterStrategy extends Strategy {

    @Override
    ActionType getAction(Samurai attacker, Samurai defender) {
        return attacker.harmBoxes < attacker.endurance + 3 ? CATCH_YOUR_BREATH : ARTFULLY_STRIKE
    }

    @Override
    ActionType getReaction(Samurai attacker, Samurai defender, ActionType actionType, List<Integer> actionOptions) {
        return DEFTLY_COUNTER
    }

    @Override
    boolean shouldInvokeFlaw(Samurai defender, ActionType actionType) {
        return !defender.flaws.isEmpty()
    }

    @Override
    boolean shouldInvokeAdvantage(Samurai attacker, ActionType actionType) {
        return !attacker.advantages.isEmpty() && actionType == CATCH_YOUR_BREATH
    }

    @Override
    List<Integer> pickOptions(Samurai attacker, Samurai defender, ActionType actionType, int optionCount) {
        switch (actionType) {
            case ARTFULLY_STRIKE:
                return [0,1,2,3]
            case DEFTLY_COUNTER:
                return [0,1,2,3]
            case CATCH_YOUR_BREATH:
                if (attacker.flaws.contains(ArtfullyStrikeAction.OFF_BALANCE))
                    return [0,3,2,1]
                else
                    return [0,2,3,1]
        }
    }
}
