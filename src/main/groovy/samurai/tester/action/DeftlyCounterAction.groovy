package samurai.tester.action

import samurai.tester.samurai.Samurai

import static samurai.tester.action.Action.ActionType.DEFTLY_COUNTER
import static samurai.tester.samurai.Samurai.Statistics.SPEED

/**
 * (R) Deftly Counter
 *   When you respond to an attack with precision and elegance, roll +speed.
 *   (1) You roll with the attack, taking one less harm from it.
 *   (2) You are unphased, if the attack would apply a flaw to you, it doesn't.
 *   (3) You gain the advantage "perfect positioning".
 *   (4) You inflict the flaw “your technique is flawed” on your attacker.
 */
class DeftlyCounterAction extends Action {

    static final String PERFECT_POSITIONING = "perfect positioning"
    static final String FLAWED_TECHNIQUE = "your technique is flawed"

    DeftlyCounterAction() {
        super(DEFTLY_COUNTER, SPEED,false, true)
    }

    @Override
    void enforcePreconditions(List<Integer> actionOptions, Samurai attacker, Samurai defender) {
        attacker.hasReacted = true
    }

    @Override
    void baseEffect(Samurai attacker, Samurai defender) {

    }

    // You roll with the attack, taking one less harm from it.
    @Override
    void option1(Samurai attacker, Samurai defender) {
        // Take no damage from the attack reacted to
        attacker.isDodging = true
    }

    // You are unphased, if the attack would apply a flaw to you, it doesn't.
    @Override
    void option2(Samurai attacker, Samurai defender) {
        attacker.isUnphased = true
    }

    // You gain the advantage "perfect positioning".
    @Override
    void option3(Samurai attacker, Samurai defender) {
        attacker.advantages.add(PERFECT_POSITIONING)
    }

    // You inflict the flaw “your technique is flawed” on your attacker.
    @Override
    void option4(Samurai attacker, Samurai defender) {
        defender.flaws.add(FLAWED_TECHNIQUE)
    }
}
