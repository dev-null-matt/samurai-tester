package samurai.tester.samurai.strategy

import samurai.tester.samurai.Samurai
import samurai.tester.samurai.Strategy
import spock.lang.Specification
import spock.lang.Unroll

import static samurai.tester.action.Action.ActionType.ARTFULLY_STRIKE
import static samurai.tester.samurai.Samurai.Statistics.DISCIPLINE
import static samurai.tester.samurai.Samurai.Statistics.ENDURANCE
import static samurai.tester.samurai.Samurai.Statistics.FORCE
import static samurai.tester.samurai.Samurai.Statistics.SPEED

class SimpleAttackStrategySpec extends Specification{

    @Unroll
    def "Picks the right number of options"() {

        given:
        Strategy subject = new SimpleAttackStrategy()
        Samurai s1 = new Samurai([FORCE, ENDURANCE, SPEED, DISCIPLINE], subject)
        Samurai s2 = new Samurai([FORCE, ENDURANCE, SPEED, DISCIPLINE], subject)

        when:
        List<Integer> options = subject.getOptions(s1, s2, ARTFULLY_STRIKE, optionCount)

        then:
        options.size() == optionCount

        where:
        optionCount | _
        0           | _
        1           | _
        2           | _
    }
}
