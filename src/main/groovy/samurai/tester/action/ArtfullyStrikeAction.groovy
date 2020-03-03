package samurai.tester.action

import samurai.tester.samurai.Samurai

import static samurai.tester.action.Action.ActionType.ARTFULLY_STRIKE
import static samurai.tester.samurai.Samurai.Statistics.FORCE

/**
 * Artfully Strike
 *   When you attack your opponent, roll +force.  On a hit, deal 1 harm and choose one, on a 10+, choose two.
 *   (1) Deal additional harm to your opponent equal to your force (to a minimum of 1).
 *   (2) You inflict the flaw “off balance” on your opponent.
 *   (3) Your opponent cannot Deftly Counter your attack.
 *   (4) You drive your opponent back from a location or away from an ally.
 */

class ArtfullyStrikeAction extends Action {

    static final String OFF_BALANCE = "off balance"

    ArtfullyStrikeAction() {
        super(ARTFULLY_STRIKE, FORCE)
    }

    @Override
    void enforcePreconditions(List<Integer> actionOptions, Samurai attacker, Samurai defender) {
        if (actionOptions.contains(2)) {
            this.canReact = false
        }
    }

    @Override
    void baseEffect(Samurai attacker, Samurai defender) {
        if (!defender.isDodging) {
            defender.harmBoxes -= 1
        }
    }

    // Deal damage to your opponent equal to your force (to a minimum of 1).
    @Override
    void option1(Samurai attacker, Samurai defender) {
        defender.harmBoxes -= Math.max(attacker.force, 1)
    }

    // You inflict the flaw “off balance” on your opponent.
    @Override
    void option2(Samurai attacker, Samurai defender) {
        //println("    $attacker.name applied \"$OFF_BALANCE\" to $defender.name")
        if (!defender.isUnphased) defender.flaws.add(OFF_BALANCE)
    }

    @Override
    void option3(Samurai attacker, Samurai defender) {
        // noop, effect applied in checkPreconditions
    }

    @Override
    void option4(Samurai attacker, Samurai defender) {

    }
}
