package samurai.tester.action

import samurai.tester.samurai.Samurai

import static samurai.tester.action.Action.ActionType.CATCH_YOUR_BREATH
import static samurai.tester.action.ArtfullyStrikeAction.OFF_BALANCE
import static samurai.tester.samurai.Samurai.Statistics.ENDURANCE

/**
 * Catch Your Breath
 *   When you take a moment to center yourself and catch your breath during a battle, roll +endurance.  On a hit,
 *   clear a harm box and choose one, on a 10+, choose two.
 *     (1) Clear an additional harm box.
 *     (2) Recover a spirit die, but reduce its size by one step.
 *     (3) Gain the advantage “resolute”.
 *     (4) Remove the flaw “off balance” from yourself.
 */
class CatchYourBreathAction extends Action {

    CatchYourBreathAction() {
        super(CATCH_YOUR_BREATH, ENDURANCE, false)
    }

    @Override
    void enforcePreconditions(List<Integer> actionOptions, Samurai attacker, Samurai defender) {

    }

    // Clear one harm box
    @Override
    void baseEffect(Samurai attacker, Samurai defender) {
        if (attacker.harmBoxes < attacker.endurance + 4) {
            attacker.harmBoxes = Math.min(attacker.harmBoxes + 1, attacker.endurance + 4)
        }
    }

    // Clear an additional harm box.
    @Override
    void option1(Samurai attacker, Samurai defender) {
        if (attacker.harmBoxes < attacker.endurance + 4) {
            attacker.harmBoxes = Math.min(attacker.harmBoxes + 1, attacker.endurance + 4)
        }
    }

    // Recover a spirit die, but reduce its size by one step.
    @Override
    void option2(Samurai attacker, Samurai defender) {

    }

    // Gain the advantage “resolute”.
    @Override
    void option3(Samurai attacker, Samurai defender) {
        attacker.advantages.add("resolute")
    }

    // Remove the flaw “off balance” from yourself.
    @Override
    void option4(Samurai attacker, Samurai defender) {
        if (attacker.flaws.contains(OFF_BALANCE)) {
            attacker.flaws.remove(OFF_BALANCE)
        }
    }
}
