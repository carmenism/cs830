import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents a heuristic function for use with A* search.
 * 
 * @author Carmen St. Jean
 *
 */
public class Heuristic {
    public enum Type {
        H0, H1_MAX, H1_SUM, H_GOAL_LITS
    }

    public static Type type;

    /**
     * Computes the heuristic value - i.e., the lower bounds estimate to goal -
     * for the given state.
     * 
     * @param state
     *            The State whose heuristic value will be computed.
     * @return The heuristic value, as a double.
     */
    public static double compute(State state) {
        HashMap<Literal, Literal> q = null;
        HashSet<Predicate> goal = Program3.goal;
        HashSet<Predicate> goalNeg = Program3.goalNeg;

        if (state.isGoal()) {
            return 0.0;
        }

        switch (type) {
        case H0:
            return 0.0;
        case H_GOAL_LITS:
            return numberGoalLits(state, goal, goalNeg);
        case H1_MAX:
            q = getH1(state, goal, goalNeg, Program3.groundedActions);

            return maxTime(q, goal, goalNeg);
        case H1_SUM:
            q = getH1(state, goal, goalNeg, Program3.groundedActions);

            return sumTimes(q, goal, goalNeg);
        }

        return 0.0;
    }

    /**
     * Computes the heuristic value for a state based on the number of goal
     * literals that are unfulfilled.
     * 
     * @param state
     *            The State whose heuristic value will be computed.
     * @param goal
     *            The literals that must be true in the goal.
     * @param goalNeg
     *            The literals that must be false in the goal.
     * @return The number of unfulfilled goal literals for the State.
     */
    private static double numberGoalLits(State state, HashSet<Predicate> goal,
            HashSet<Predicate> goalNeg) {
        int num = 0;

        // Count the number of goal literals that are false.
        for (Predicate p : goal) {
            if (!state.getPositive().contains(p)) {
                num = num + 1;
            }
        }

        // Count the number of goalneg literals that are true.
        for (Predicate p : goalNeg) {
            if (!state.getNegative().contains(p)) {
                num = num + 1;
            }
        }

        return num;
    }

    /**
     * Prepares the HashMap necessary for calculating the H1 heuristic for
     * STRIPS planning.
     * 
     * @param state
     *            The State whose heuristic value will be computed.
     * @param goal
     *            The literals that must be true in the goal.
     * @param goalNeg
     *            The literals that must be false in the goal.
     * @param allActions
     *            All of the grounded Actions for the problem.
     * @return The HashMap necessary for H1.
     */
    private static HashMap<Literal, Literal> getH1(State state,
            HashSet<Predicate> goal, HashSet<Predicate> goalNeg,
            HashSet<Action> allActions) {
        int time = 0;
        HashMap<Literal, Literal> q = getInitialQueue(state);

        while (!allGoalsTrue(q, goal, goalNeg) && !q.isEmpty()) {
            HashMap<Literal, Literal> qPrime = new HashMap<Literal, Literal>();

            HashSet<Predicate> positive = getPositive(q);
            HashSet<Predicate> negative = getNegative(q);

            // Check each literal in q.
            for (Literal lit : q.values()) {
                // Check if each action where lit is a precondition.
                for (Action a : lit.actionsWherePrecondition(allActions)) {
                    // If the action is completely possible now, add its effects
                    // to qPrime.
                    if (a.possible(positive, negative)) {
                        // "Delete" literals from qPrime.
                        for (Predicate p : a.getDel()) {
                            Literal newLit = new Literal(time + 1, p, false);

                            if (!q.containsKey(newLit)) {
                                qPrime.put(newLit, newLit);
                            }
                        }

                        // Add literals to qPrime.
                        for (Predicate p : a.getAdd()) {
                            Literal newLit = new Literal(time + 1, p, true);

                            if (!q.containsKey(newLit)) {
                                qPrime.put(newLit, newLit);
                            }
                        }
                    }
                }
            }

            time = time + 1;

            q.putAll(qPrime);
        }

        return q;
    }

    /**
     * Computes the H1 max heuristic - i.e., the maximum time it took for a goal
     * literal to become true - for the given State.
     * 
     * @param h1
     *            The HashMap for H1.
     * @param goal
     *            The literals that must be true in the goal.
     * @param goalNeg
     *            The literals that must be false in the goal.
     * @return The heuristic value for the State.
     */
    private static double maxTime(HashMap<Literal, Literal> h1,
            HashSet<Predicate> goal, HashSet<Predicate> goalNeg) {
        double max = Double.MIN_VALUE;

        if (h1.isEmpty()) {
            return Double.MAX_VALUE;
        }

        for (Predicate p : goal) {
            Literal tempLit = new Literal(0, p, true);
            Literal lit = h1.get(tempLit);
            int t = lit.getTimeBecameTrue();

            if (max < t) {
                max = t;
            }
        }

        for (Predicate p : goalNeg) {
            Literal tempLit = new Literal(0, p, false);
            Literal lit = h1.get(tempLit);
            int t = lit.getTimeBecameTrue();

            if (max < t) {
                max = t;
            }
        }

        return max;
    }

    /**
     * Computes the H1 sum heuristic - i.e., the sum of times it took for each
     * goal literal to become true - for the given State.
     * 
     * @param h1
     *            The HashMap for H1.
     * @param goal
     *            The literals that must be true in the goal.
     * @param goalNeg
     *            The literals that must be false in the goal.
     * @return The heuristic value for the State.
     */
    private static double sumTimes(HashMap<Literal, Literal> h1,
            HashSet<Predicate> goal, HashSet<Predicate> goalNeg) {
        double sum = 0.0;

        if (h1.isEmpty()) {
            return Double.MAX_VALUE;
        }

        for (Predicate p : goal) {
            Literal tempLit = new Literal(0, p, true);
            Literal lit = h1.get(tempLit);

            sum = sum + lit.getTimeBecameTrue();
        }

        for (Predicate p : goalNeg) {
            Literal tempLit = new Literal(0, p, false);
            Literal lit = h1.get(tempLit);

            sum = sum + lit.getTimeBecameTrue();
        }

        return sum;
    }

    /**
     * Check to see if all literals of the goal are fulfilled.
     * 
     * @param queue
     *            The queue of true and false literals.
     * @param goal
     *            The literals that must be true in the goal.
     * @param goalNeg
     *            The literals that must be false in the goal.
     * @return True if the goal is fulfilled; false otherwise.
     */
    private static boolean allGoalsTrue(HashMap<Literal, Literal> queue,
            HashSet<Predicate> goal, HashSet<Predicate> goalNeg) {
        for (Predicate p : goal) {
            Literal tempLit = new Literal(0, p, true);

            if (!queue.containsKey(tempLit)) {
                return false;
            }
        }

        for (Predicate p : goalNeg) {
            Literal tempLit = new Literal(0, p, false);

            if (!queue.containsKey(tempLit)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the set of Predicates from the queue which are indicated as having
     * the value of true.
     * 
     * @param queue
     *            The queue of true and false literals.
     * @return A HashSet of Predicates corresponding with true Literals.
     */
    private static HashSet<Predicate> getPositive(
            HashMap<Literal, Literal> queue) {
        HashSet<Predicate> positive = new HashSet<Predicate>();

        for (Literal lit : queue.values()) {
            if (lit.isPositive()) {
                positive.add(lit.getPredicate());
            }
        }

        return positive;
    }

    /**
     * Get the set of Predicates from the queue which are indicated as having
     * the value of false.
     * 
     * @param queue
     *            The queue of true and false literals.
     * @return A HashSet of Predicates corresponding with false Literals.
     */
    private static HashSet<Predicate> getNegative(
            HashMap<Literal, Literal> queue) {
        HashSet<Predicate> negative = new HashSet<Predicate>();

        for (Literal lit : queue.values()) {
            if (!lit.isPositive()) {
                negative.add(lit.getPredicate());
            }
        }

        return negative;
    }

    /**
     * Get the initial queue for the building of H1.
     * 
     * @param state
     *            The State for which the heuristic value is going to be
     *            calculated.
     * @return The HashMap of Literals (mapped by literals) according to the
     *         true and false Predicates of the State.
     */
    private static HashMap<Literal, Literal> getInitialQueue(State state) {
        HashMap<Literal, Literal> literals = new HashMap<Literal, Literal>();

        // Build true Literals for all positive Predicates.
        for (Predicate p : state.getPositive()) {
            Literal lit = new Literal(0, p, true);

            literals.put(lit, lit);
        }

        // Builds false Literals for all negative Predicates.
        for (Predicate p : state.getNegative()) {
            Literal lit = new Literal(0, p, false);

            literals.put(lit, lit);
        }

        return literals;
    }

    /**
     * Represents a Literal, that is a Predicate and a value specifying true or
     * false, for use with calculating H1 heuristic values.
     * 
     * The Predicate class was insufficient because its value of true or false
     * is indicated by its presense in the positive or negative lists,
     * respectively, for a State. The calculation of H1 requires that both true
     * and false versions of a Predicate may be present at once.
     * 
     * @author Carmen St. Jean
     * 
     */
    private static class Literal {
        private final int timeBecameTrue;
        private final Predicate predicate;
        private final boolean positive;

        /**
         * Creates a Literal from a Predicate, a boolean indicating whether or
         * not the Predicate is true or false, and the time the Predicate with
         * this truth value became true.
         * 
         * @param timeBecameTrue
         *            The time this Literal became "true".
         * @param predicate
         *            The Predicate.
         * @param positive
         *            True if the Predicate is true (e.g., P(A,B), false if the
         *            Predicate is not true (e.g., ~P(A,B)).
         */
        public Literal(int timeBecameTrue, Predicate predicate, 
                boolean positive) {
            this.timeBecameTrue = timeBecameTrue;
            this.predicate = predicate;
            this.positive = positive;
        }

        /**
         * Get a HashSet of Actions for which this Literal is a pre-condition
         * (if positive) or a preneg-condition (if negative).
         * 
         * @param allActions
         *            A HashSet of all grounded actions.
         * @return A HashSet of possibly reachable actions.
         */
        public HashSet<Action> actionsWherePrecondition(
                HashSet<Action> allActions) {
            if (positive) {
                return predicate.actionsWherePrecondition(allActions);
            }

            return predicate.actionsWherePrenegcondition(allActions);
        }

        public boolean isPositive() {
            return positive;
        }

        public int getTimeBecameTrue() {
            return timeBecameTrue;
        }

        public Predicate getPredicate() {
            return predicate;
        }

        @Override
        public String toString() {
            return positive + " " + predicate;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (positive ? 1231 : 1237);
            result = prime * result
                    + ((predicate == null) ? 0 : predicate.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Literal other = (Literal) obj;
            if (positive != other.positive)
                return false;
            if (predicate == null) {
                if (other.predicate != null)
                    return false;
            } else if (!predicate.equals(other.predicate))
                return false;
            return true;
        }
    }
}
