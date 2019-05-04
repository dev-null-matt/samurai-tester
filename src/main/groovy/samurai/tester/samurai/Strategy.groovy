package samurai.tester.samurai

import static samurai.tester.action.Action.ActionType

/*
 * 5/2
 * AttackAndMonopolizeStrategy    14.2925%
 * ChainCounterStrategy           29.0357%
 * CounterAndMonopolizeStrategy   27.1718%
 * DebuffAndMonopolizeStrategy    18.3783%
 * SimpleAttackStrategy           11.1217%
 */

abstract class Strategy {

    abstract ActionType getAction(Samurai attacker, Samurai defender)

    abstract ActionType getReaction(Samurai attacker, Samurai defender, ActionType actionType, List<Integer> actionOptions)

    boolean shouldInvokeAdvantage(Samurai attacker, ActionType actionType) {
        return false
    }

    boolean shouldInvokeFlaw(Samurai defender, ActionType actionType) {
        return false
    }

    def removeAdvantageFromSelf(Samurai samurai) {
        if (!samurai.advantages.isEmpty()) samurai.advantages.remove(0)
    }

    def removeFlawFromSelf(Samurai samurai) {
        if (!samurai.flaws.empty) samurai.flaws.remove(0)
    }

    def removeFlawFromOpponent(Samurai samurai) {
        if (!samurai.flaws.empty) samurai.flaws.remove(0)
    }

    abstract List<Integer> pickOptions(Samurai attacker, Samurai defender, ActionType actionType, int optionCount)

    List<Integer> getOptions(Samurai attacker, Samurai defender, ActionType actionType, int optionCount) {
        if (optionCount == 0) {
            return []
        } else {
            return pickOptions(attacker, defender, actionType, optionCount)[0 .. optionCount-1]
        }
    }

    @Override
    String toString() {
        this.class.getSimpleName()
    }
}
