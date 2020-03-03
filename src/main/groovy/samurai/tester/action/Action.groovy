package samurai.tester.action

import samurai.tester.samurai.Samurai

import static samurai.tester.samurai.Samurai.Statistics

abstract class Action {

    ActionType actionType
    Statistics primaryStat
    boolean canReact
    boolean isReaction

    Action(ActionType actionType, Statistics primaryStat, boolean canReact = true, boolean isReaction = false) {
        this.actionType = actionType
        this.primaryStat = primaryStat
        this.canReact = canReact
        this.isReaction = isReaction
    }

    void perform(Samurai attacker, Samurai defender) {

        int optionCount = roll(
                getActiveStatisticValue(attacker),
                hasAdvantage(attacker, defender),
                hasDisadvantage(attacker)
        )

        List<Integer> actionOptions = optionCount == 0 ? [] : attacker.strategy.getOptions(attacker, defender, actionType, optionCount)[0 .. optionCount-1]

        enforcePreconditions(actionOptions, attacker, defender)
        handleReaction(defender, attacker, actionOptions)
        applyEffects(attacker, defender, actionOptions)

        defender.isDodging = false
        defender.isUnphased = false
    }

    void handleReaction(Samurai defender, Samurai attacker, List<Integer> actionOptions) {

        ActionType reactionType = defender.strategy.getReaction(attacker, defender, actionType, actionOptions)

        if (canReact && reactionType && reactionType == ActionType.DEFTLY_COUNTER) {

            reaction.perform(defender, attacker)
        }
    }

    Action getReaction() {
        return new DeftlyCounterAction()
    }

    void applyEffects(Samurai attacker, Samurai defender, List<Integer> actionOptions) {

        baseEffect(attacker, defender)

        for (Integer actionOption : actionOptions) {
            switch (actionOption) {
                case 0:
                    option1(attacker, defender)
                    break
                case 1:
                    option2(attacker, defender)
                    break
                case 2:
                    option3(attacker, defender)
                    break
                case 3:
                    option4(attacker, defender)
                    break
                default:
                    break
            }
        }
    }

    boolean hasDisadvantage(Samurai attacker) {

        switch (actionType) {
            case ActionType.DEFTLY_COUNTER:
                return attacker.hasActed
            default:
                return attacker.hasReacted
        }
    }

    boolean hasAdvantage(Samurai attacker, Samurai defender) {

        boolean shouldInvokeFlaw =
                    actionType != ActionType.CATCH_YOUR_BREATH &&
                    attacker.strategy.shouldInvokeFlaw(defender, actionType) &&
                    !defender.flaws.isEmpty()

        boolean shouldInvokeAdvantage =
                    !shouldInvokeFlaw &&
                    attacker.strategy.shouldInvokeAdvantage(attacker, actionType) &&
                    !attacker.advantages.isEmpty()

        if (shouldInvokeFlaw) {
            attacker.strategy.removeFlawFromOpponent(defender)
        } else if (shouldInvokeAdvantage) {
            attacker.strategy.removeAdvantageFromSelf(attacker)
        }

        boolean hasAdvantage = shouldInvokeFlaw || shouldInvokeAdvantage
        hasAdvantage
    }

    int getActiveStatisticValue(Samurai samurai) {

        switch (primaryStat) {
            case Statistics.DISCIPLINE:
                return samurai.endurance
            case Statistics.ENDURANCE:
                return samurai.endurance
            case Statistics.FORCE:
                return samurai.force
            case Statistics.SPEED:
                return samurai.speed
            default:
                return 0
        }
    }

    abstract void enforcePreconditions(List<Integer> actionOptions, Samurai attacker, Samurai defender)
    abstract void baseEffect(Samurai attacker, Samurai defender)
    abstract void option1(Samurai attacker, Samurai defender)
    abstract void option2(Samurai attacker, Samurai defender)
    abstract void option3(Samurai attacker, Samurai defender)
    abstract void option4(Samurai attacker, Samurai defender)

    final int roll(int activeStat, boolean hasAdvantage = false, boolean hasDisadvantage = false) {

        int roll1 = Math.floor(Math.random() * 6) + 1
        int roll2 = Math.floor(Math.random() * 6) + 1
        int roll3 = Math.floor(Math.random() * 6) + 1

        return getOptionCount(hasAdvantage, hasDisadvantage, roll1, roll2, roll3, activeStat)
    }

    int getOptionCount(boolean hasAdvantage, boolean hasDisadvantage, int roll1, int roll2, int roll3, int activeStat) {

        if (hasAdvantage && !hasDisadvantage) {
            if (roll1 > roll2) {
                roll2 = Math.max(roll2, roll3)
            } else {
                roll1 = Math.max(roll1, roll3)
            }
        } else if (hasDisadvantage && !hasAdvantage) {
            if (roll1 < roll2) {
                roll2 = Math.min(roll2, roll3)
            } else {
                roll1 = Math.min(roll1, roll3)
            }
        }

        return getOptionCount(roll1, roll2, activeStat)
    }

    int getOptionCount(int roll1, int roll2, int activeStat) {

        if (roll1 + roll2 + activeStat > 9) {
            return 2
        } else if (roll1 + roll2 + activeStat > 6) {
            return 1
        } else {
            return 0
        }
    }

    enum ActionType {
        ARTFULLY_STRIKE,
        CATCH_YOUR_BREATH,
        DEFTLY_COUNTER,
        MONOPOLIZE_OPENING,
    }
}