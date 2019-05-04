package samurai.tester.samurai.strategy

import samurai.tester.action.Action
import samurai.tester.samurai.Samurai
import samurai.tester.samurai.Strategy

import static samurai.tester.action.Action.ActionType
import static samurai.tester.action.Action.ActionType.ARTFULLY_STRIKE

class SimpleAttackStrategy extends Strategy {

    @Override
    ActionType getAction(Samurai attacker, Samurai defender) {
        return ARTFULLY_STRIKE
    }

    @Override
    List<Integer> pickOptions(Samurai attacker, Samurai defender, ActionType actionType, int optionCount) {
        if (defender.speed > 1)
            return [0, 2, 1, 3]
        else
            return [0, 1, 2, 3]
    }

    @Override
    boolean shouldInvokeFlaw(Samurai defender, ActionType actionType) {
        return defender.flaws.size() > 0
    }

    @Override
    ActionType getReaction(Samurai attacker, Samurai defender, Action.ActionType actionType, List<Integer> actionOptions) {
        return null
    }
}
