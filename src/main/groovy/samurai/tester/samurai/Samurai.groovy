package samurai.tester.samurai

class Samurai {

    // Statistics: Assign +2, +1, 0, -1 to your statistics
    static final int[] STAT_ARRAY = [2,1,0,-1]

    String name

    int endurance
    int discipline
    int force
    int speed

    Set<String> advantages
    Set<String> flaws

    Strategy strategy

    // You can make one action during your turn.  If you do, any reaction you make before your next turn will have disadvantage.
    boolean hasActed

    // You can make one reaction between each time it's your turn.  If you do, you have disadvantage on your action during your turn.
    boolean hasReacted

    // If you're dodging, an attack can't deal you damage
    boolean isDodging

    // If you're unphased, you can't be afflicted by a flaw
    boolean isUnphased

    // Harm Boxes: You start with a number of harm boxes equal to four plus your endurance score.
    int harmBoxes

    Samurai(List<Statistics> stats, Strategy strategy) {

        StringBuilder nameBuilder = new StringBuilder()

        for (int i = 0; i < STAT_ARRAY.length; i++) {
            switch (stats.get(i)) {
                case Statistics.ENDURANCE:
                    nameBuilder.append("E")
                    endurance = STAT_ARRAY[i]
                    break
                case Statistics.DISCIPLINE:
                    nameBuilder.append("D")
                    discipline = STAT_ARRAY[i]
                    break
                case Statistics.FORCE:
                    nameBuilder.append("F")
                    force = STAT_ARRAY[i]
                    break
                case Statistics.SPEED:
                    nameBuilder.append("S")
                    speed = STAT_ARRAY[i]
                    break
                default:
                    break
            }
        }

        this.name = nameBuilder.toString()

        hasActed = false
        hasReacted = false
        harmBoxes = endurance + 4

        advantages = new HashSet<>()
        flaws = new HashSet<>()

        isDodging = false
        isUnphased = false

        this.strategy = strategy
    }

    private Samurai(String name, int discipline, int endurance, int force, int speed, Strategy strategy) {
        this.name = name
        this.discipline = discipline
        this.endurance = endurance
        this.force = force
        this.speed = speed
        this.strategy = strategy

        hasReacted = false
        harmBoxes = endurance + 4

        advantages = new HashSet<>()
        flaws = new HashSet<>()
    }

    Samurai clone() {
        return new Samurai(name, discipline, endurance, force, speed, strategy)
    }

    @Override
    String toString() {
        //return "$name - $strategy"
        return "$strategy"
    }

    enum Statistics {
        ENDURANCE,
        DISCIPLINE,
        FORCE,
        SPEED,
    }
}
