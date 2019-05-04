package samurai.tester.samurai.strategy

import samurai.tester.action.Action
import samurai.tester.action.Action.ActionType
import samurai.tester.samurai.Samurai
import samurai.tester.samurai.Strategy

import static samurai.tester.action.Action.ActionType.ARTFULLY_STRIKE
import static samurai.tester.action.Action.ActionType.DEFTLY_COUNTER
import static samurai.tester.action.Action.ActionType.MONOPOLIZE_OPENING

class DebuffAndMonopolizeStrategy extends Strategy {

    @Override
    ActionType getAction(Samurai attacker, Samurai defender) {
        return defender.flaws.isEmpty() ? ARTFULLY_STRIKE : MONOPOLIZE_OPENING
    }

    @Override
    List<Integer> pickOptions(Samurai attacker, Samurai defender, ActionType actionType, int optionCount) {
        switch (actionType) {
            case ARTFULLY_STRIKE:
                return [1, 0, 2, 3]
            case DEFTLY_COUNTER:
                return [0, 2, 1, 3]
            case MONOPOLIZE_OPENING:
                return [0, 1, 2, 3]
        }
    }

    @Override
    ActionType getReaction(Samurai attacker, Samurai defender, Action.ActionType actionType, List<Integer> actionOptions) {
        return DEFTLY_COUNTER
    }
}
