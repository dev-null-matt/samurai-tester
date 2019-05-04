package samurai.tester.action

import samurai.tester.samurai.Samurai

import static samurai.tester.action.Action.ActionType.MONOPOLIZE_OPENING
import static samurai.tester.samurai.Samurai.Statistics.DISCIPLINE

/**
 * Monopolize Opening
 *   When you use a trait your opponent is afflicted with as an opening, roll +discipline.  On a hit, your target can’t take a reaction and choose one, on a 10+, choose two.
 *   (1) Deal 2 harm to your opponent.
 *   (2) Remove any temporary flaw from yourself.
 *   (3) Remove any temporary trait from your surroundings.
 *   (4) Your opponent has disadvantage on their next action unless it is to attack you.
 *   After resolving the attack, remove the trait from your opponent that you took advantage of.
 */
class MonopolizeOpeningAction extends Action {

    MonopolizeOpeningAction() {
        super(MONOPOLIZE_OPENING, DISCIPLINE, false)
    }

    @Override
    int getActiveStatisticValue(Samurai samurai) {
        return samurai.discipline
    }

    @Override
    void enforcePreconditions(List<Integer> actionOptions, Samurai attacker, Samurai defender) {

        assert(!defender.flaws.isEmpty())

        attacker.strategy.removeFlawFromOpponent(defender)
    }

    @Override
    void baseEffect(Samurai attacker, Samurai defender) {

    }

    // Deal 2 harm to your opponent.
    @Override
    void option1(Samurai attacker, Samurai defender) {
        defender.harmBoxes -= 2
    }

    // Remove any temporary flaw from yourself.
    @Override
    void option2(Samurai attacker, Samurai defender) {
        attacker.strategy.removeFlawFromSelf(attacker)
    }

    @Override
    void option3(Samurai attacker, Samurai defender) {

    }

    @Override
    void option4(Samurai attacker, Samurai defender) {

    }
}
