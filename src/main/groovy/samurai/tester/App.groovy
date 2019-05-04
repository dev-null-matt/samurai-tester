/*
 * This Groovy source file was generated by the Gradle 'init' task.
 */
package samurai.tester

import samurai.tester.action.Action
import samurai.tester.action.ArtfullyStrikeAction
import samurai.tester.action.CatchYourBreathAction
import samurai.tester.action.MonopolizeOpeningAction
import samurai.tester.samurai.Samurai
import samurai.tester.samurai.Strategy
import samurai.tester.samurai.strategy.AttackAndMonopolizeStrategy
import samurai.tester.samurai.strategy.ChainCounterStrategy
import samurai.tester.samurai.strategy.CounterAndMonopolizeStrategy
import samurai.tester.samurai.strategy.DebuffAndMonopolizeStrategy
import samurai.tester.samurai.strategy.SimpleAttackStrategy

import static samurai.tester.action.Action.ActionType
import static samurai.tester.samurai.Samurai.Statistics
import static samurai.tester.samurai.Samurai.Statistics.DISCIPLINE
import static samurai.tester.samurai.Samurai.Statistics.ENDURANCE
import static samurai.tester.samurai.Samurai.Statistics.FORCE
import static samurai.tester.samurai.Samurai.Statistics.SPEED

class App {

    static void main(String[] args) {

        int iterations = 100000

        Map<String, Integer> results = new HashMap<>()

        List<Statistics> stats = [FORCE, ENDURANCE, SPEED, DISCIPLINE]
        List<Strategy> strategies = [
                new AttackAndMonopolizeStrategy(),
                new ChainCounterStrategy(),
                new CounterAndMonopolizeStrategy(),
                new DebuffAndMonopolizeStrategy(),
                new SimpleAttackStrategy()
        ]

        for (int i = 1; i <= iterations; i++) {

            Samurai s1
            Samurai s2

            Collections.shuffle(stats)
            s1 = new Samurai(stats, strategies[Math.random() * strategies.size()])

            Collections.shuffle(stats)
            while (s2?.strategy == null || s1.strategy == s2.strategy) {
                s2 = new Samurai(stats, strategies[Math.random() * strategies.size()])
            }

            String resultString = null

            if (i % 2 == 0) {
                runDuel(s1, s2)
            } else {
                runDuel(s2, s1)
            }

            if (s1.harmBoxes > 0) {
                resultString = s1.toString()
            } else if (s2.harmBoxes > 0) {
                resultString = s2.toString()
            }

            if (resultString && results.containsKey(resultString)) {
                results.put(resultString, results.get(resultString)+1)
            } else if (resultString) {
                results.put(resultString, 1)
            }

            if (i % 1000 == 0)
                System.out.print(i % 10000 == 0 ? ".\n" : ".")
        }

        results.keySet().each {
            println( "'${it.split(' - ')[0]},'${it.split(' - ')[1]},${results.get(it)}")
        }
    }

    static void runDuel(Samurai s1, Samurai s2) {

        while (s1.harmBoxes > 0 && s2.harmBoxes > 0) {
            takeTurn(s1.strategy.getAction(s1, s2), s1, s2)
            if (s2.harmBoxes < 1) break
            takeTurn(s2.strategy.getAction(s2, s1), s2, s1)
        }
    }

    static void takeTurn(ActionType actionType, Samurai attacker, Samurai defender) {

        Action action = null
        attacker.hasActed = false

        switch (actionType) {
            case ActionType.ARTFULLY_STRIKE:
                action = new ArtfullyStrikeAction()
                attacker.hasActed = true
                break
            case ActionType.MONOPOLIZE_OPENING:
                action = new MonopolizeOpeningAction()
                attacker.hasActed = true
                break
            case ActionType.CATCH_YOUR_BREATH:
                action = new CatchYourBreathAction()
                attacker.hasActed = true
                break
            default:
                break
        }

        action?.perform(attacker, defender)
        attacker.hasReacted = false
    }
}