package samurai.tester.samurai

import samurai.tester.samurai.strategy.SimpleAttackStrategy
import spock.lang.Specification
import spock.lang.Unroll

import static samurai.tester.samurai.Samurai.Statistics.*

class SamuraiSpec extends Specification {

    @Unroll
    def "Properly sets stats when creating samurai"() {

        when:
        def samurai = new Samurai(stats, new SimpleAttackStrategy())

        then:
        samurai.harmBoxes == harmBoxes
        samurai.discipline == discipline
        samurai.endurance == endurance
        samurai.force == force
        samurai.speed == speed

        where:
        stats                                 | harmBoxes | discipline | endurance | force | speed
        [DISCIPLINE, ENDURANCE, FORCE, SPEED] | 5         | 2          | 1         | 0     | -1
        [SPEED, FORCE, ENDURANCE, DISCIPLINE] | 4         | -1         | 0         | 1     | 2
    }
}
