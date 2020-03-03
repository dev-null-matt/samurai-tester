package samurai.tester.action

import samurai.tester.samurai.Samurai
import samurai.tester.samurai.Strategy
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static samurai.tester.samurai.Samurai.Statistics
import static samurai.tester.samurai.Samurai.Statistics.DISCIPLINE
import static samurai.tester.samurai.Samurai.Statistics.ENDURANCE
import static samurai.tester.samurai.Samurai.Statistics.FORCE
import static samurai.tester.samurai.Samurai.Statistics.SPEED

class ActionSpec extends Specification {

    @Shared
    def attacker

    @Shared
    def defender

    @Shared
    def attackerStrategy

    @Shared
    def defenderStrategy

    def setup() {

        attackerStrategy = Mock(Strategy.class)
        attacker = new Samurai([FORCE, ENDURANCE, SPEED, DISCIPLINE], attackerStrategy)

        defenderStrategy = Mock(Strategy.class)
        defender = new Samurai([FORCE, ENDURANCE, SPEED, DISCIPLINE], defenderStrategy)
    }

    def "basic contract met"() {

        given:
        def subject = new ActionSpy(null)

        when:
        subject.perform(attacker, defender)

        then:
        attacker.advantages >> []
        attacker.strategy.getOptions(attacker, defender, null, _) >> [0, 1]
        attacker.strategy.shouldInvokeAdvantage(attacker, null) >> false
        attacker.strategy.shouldInvokeFlaw(defender, null) >> false
        defender.flaws >> []

        and:
        subject.preConditionsEnforced
        subject.baseEffectApplied
    }

    @Unroll
    def "handleReaction() #description"() {

        given:
        def subject = new ActionSpy(null)
        def reaction = new DeftlyCounterAction()
        subject.child = reaction
        subject.canReact = canReact

        when:
        subject.handleReaction(defender, attacker, actionOptions)

        then:
        defender.strategy.getReaction(attacker, defender, null, actionOptions) >> reactionType

        // Otherwise it all burns down if the strategies actually get called
        defender.advantages >> []
        defender.strategy.getOptions(defender, attacker, _, _) >> [0, 1]
        defender.strategy.shouldInvokeAdvantage(defender, null) >> false
        defender.strategy.shouldInvokeFlaw(attacker, null) >> false
        attacker.flaws >> []

        and:
        !attacker.hasActed
        !attacker.hasReacted
        !defender.hasActed
        defender.hasReacted == reacted

        where:
        description   | actionOptions | canReact | reactionType                     | reacted
        "No reaction" | []            | true     | null                             | false
        "Woulda"      | []            | false    | Action.ActionType.DEFTLY_COUNTER | false
        "Reacted"     | []            | true     | Action.ActionType.DEFTLY_COUNTER | true
    }

    @Unroll
    def "applyEffect() #description"() {

        given:
        def subject = new ActionSpy(null)

        when:
        subject.applyEffects(attacker, defender, actionOptions)

        then:
        subject.oneInvoked == o1
        subject.twoInvoked == o2
        subject.threeInvoked == o3
        subject.fourInvoked == o4

        where:
        description  | actionOptions | o1    | o2    | o3    | o4
        "Empty opts" | []            | false | false | false | false
        "0"          | [0]           | true  | false | false | false
        "1"          | [1]           | false | true  | false | false
        "2"          | [2]           | false | false | true  | false
        "3"          | [3]           | false | false | false | true
        "0,1"        | [0, 1]        | true  | true  | false | false
        "0,2"        | [0, 2]        | true  | false | true  | false
        "0,3"        | [0, 3]        | true  | false | false | true
        "1,2"        | [1, 2]        | false | true  | true  | false
        "1,3"        | [1, 3]        | false | true  | false | true
        "2,3"        | [2, 3]        | false | false | true  | true
        "0,1,2"      | [0, 1, 2]     | true  | true  | true  | false
        "0,1,3"      | [0, 1, 3]     | true  | true  | false | true
        "1,2,3"      | [1, 2, 3]     | false | true  | true  | true
    }

    @Unroll
    def "hasAdvantage() #description"() {

        given:
        def subject = new ActionSpy(null)
        attacker.advantages = advantages
        defender.flaws = flaws

        when:
        def result = subject.hasAdvantage(attacker, defender)

        then:
        attacker.strategy.shouldInvokeAdvantage(_, _) >> attackerShouldInvokeAdvantage
        attacker.strategy.shouldInvokeFlaw(_, _) >> attackerShouldInvokeFlaw

        and:
        result == hasAdvantage

        where:
        description        | attackerShouldInvokeAdvantage | advantages    | attackerShouldInvokeFlaw | flaws         | hasAdvantage
        'invoke neither'   | false                         | []            | false                    | []            | false
        'invoke flaw'      | false                         | []            | true                     | Set.of("foo") | true
        'invoke advantage' | true                          | Set.of("foo") | true                     | []            | true
    }

    @Unroll
    def "hasDisadvantage() #description"() {

        given:
        def subject = new ActionSpy(null)
        subject.actionType = actionType

        when:
        def result = subject.hasDisadvantage(attacker)

        then:
        attacker.hasActed >> hasActed
        attacker.hasReacted >> hasReacted

        and:
        result == expected

        where:
        description | actionType                        | hasActed | hasReacted | expected
        "AS --"     | Action.ActionType.ARTFULLY_STRIKE | false    | false      | false
        "AS +-"     | Action.ActionType.ARTFULLY_STRIKE | true     | false      | false
        "AS -+"     | Action.ActionType.ARTFULLY_STRIKE | false    | true       | true
        "AS ++"     | Action.ActionType.ARTFULLY_STRIKE | true     | true       | true
        "DC --"     | Action.ActionType.DEFTLY_COUNTER  | false    | false      | false
        "DC +-"     | Action.ActionType.DEFTLY_COUNTER  | true     | false      | true
        "DC -+"     | Action.ActionType.DEFTLY_COUNTER  | false    | true       | false
        "DC ++"     | Action.ActionType.DEFTLY_COUNTER  | true     | true       | true
    }

    @Unroll
    def "getOptionCount (rolls + stat) #description"() {

        given:
        def subject = new ActionSpy(null)

        when:
        def result = subject.getOptionCount(roll1, roll2, statScore)

        then:
        result == expected

        where:
        description  | roll1 | roll2 | statScore | expected
        "snake eyes" | 1     | 1     | 0         | 0
        "3+3+1"      | 3     | 3     | 1         | 1
        "4+4+2"      | 4     | 4     | 2         | 2
    }

    @Unroll
    def "getOptionCount (lots of params) #description"() {

        given:
        def subject = new ActionSpy(null)

        when:
        def result = subject.getOptionCount(adv, dis, roll1, roll2, roll3, statScore)

        then:
        result == expected

        where:
        description        | adv   | dis   | roll1 | roll2 | roll3 | statScore | expected
        "2,2,2 +2"         | false | false | 2     | 2     | 2     | 2         | 0
        "3,3,3 +1"         | false | false | 3     | 3     | 3     | 1         | 1
        "adv 1,1,5 +1"     | true  | false | 1     | 1     | 5     | 1         | 1
        "dis 4,4,1 +0"     | false | true  | 4     | 4     | 1     | 0         | 0
        "adv+dis 2,2,3 +2" | true  | true  | 2     | 2     | 3     | 2         | 0
    }

    class ActionSpy extends Action {

        boolean performInvoked = false

        boolean oneInvoked = false
        boolean twoInvoked = false
        boolean threeInvoked = false
        boolean fourInvoked = false

        boolean preConditionsEnforced = false
        boolean baseEffectApplied = false

        Action parent = null
        Action child = null

        ActionSpy(Statistics primaryStat, ActionSpy child = null, ActionSpy parent = null) {
            super(null, primaryStat, false, false)
            this.child = child
            this.parent = parent
        }

        @Override
        void perform(Samurai attacker, Samurai defender) {
            performInvoked = true
            super.perform(attacker, defender)
        }

        @Override
        void enforcePreconditions(List<Integer> actionOptions, Samurai attacker, Samurai defender) {
            preConditionsEnforced = true
        }

        @Override
        void baseEffect(Samurai attacker, Samurai defender) {
            baseEffectApplied = true
        }

        @Override
        void option1(Samurai attacker, Samurai defender) {
            oneInvoked = true
        }

        @Override
        void option2(Samurai attacker, Samurai defender) {
            twoInvoked = true
        }

        @Override
        void option3(Samurai attacker, Samurai defender) {
            threeInvoked = true
        }

        @Override
        void option4(Samurai attacker, Samurai defender) {
            fourInvoked = true
        }

        @Override
        Action getReaction() {
            return child
        }
    }
}
