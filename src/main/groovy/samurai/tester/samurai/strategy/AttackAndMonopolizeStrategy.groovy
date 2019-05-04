package samurai.tester.samurai.strategy

import samurai.tester.action.Action
import samurai.tester.samurai.Samurai
import samurai.tester.samurai.Strategy

import static samurai.tester.action.Action.ActionType
import static samurai.tester.action.Action.ActionType.ARTFULLY_STRIKE
import static samurai.tester.action.Action.ActionType.MONOPOLIZE_OPENING

class AttackAndMonopolizeStrategy extends Strategy {

    @Override
    Action.ActionType getAction(Samurai attacker, Samurai defender) {
        return defender.flaws.isEmpty() ? ARTFULLY_STRIKE : MONOPOLIZE_OPENING
    }

    @Override
    List<Integer> pickOptions(Samurai attacker, Samurai defender, ActionType actionType, int optionCount) {

        switch (actionType) {
            case ARTFULLY_STRIKE:
                return [0, 1, 2, 3]
            case MONOPOLIZE_OPENING:
                return [0, 1, 2, 3]
        }
    }

    @Override
    Action.ActionType getReaction(Samurai attacker, Samurai defender, ActionType actionType, List<Integer> actionOptions) {
        return null
    }
}
