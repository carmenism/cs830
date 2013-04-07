import java.util.HashMap;
import java.util.HashSet;

public class Heuristic {
    public enum Type {
        H0, H1_MAX, H1_SUM, H_GOAL_LITS
    }

    public static Type type;

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
            q = getH1(state, goal, goalNeg);

            return maxTime(q, goal, goalNeg);
        case H1_SUM:
            q = getH1(state, goal, goalNeg);

            return sumTimes(q, goal, goalNeg);
        }

        return 0.0;
    }

    private static double numberGoalLits(State state, HashSet<Predicate> goal,
            HashSet<Predicate> goalNeg) {
        int num = 0;
        
        for (Predicate p : goal) {
            if (!state.getPositive().contains(p)) {
                num = num + 1;
            }
        }

        for (Predicate p : goalNeg) {
            if (!state.getNegative().contains(p)) {
                num = num + 1;
            }
        }
        
        return num;
    }

    private static HashMap<Literal, Literal> getH1(State state,
            HashSet<Predicate> goal, HashSet<Predicate> goalNeg) {
        int time = 0;
        HashMap<Literal, Literal> q = getInitialQueue(state);
        
        while (!allGoalsTrue(q, goal, goalNeg) && !q.isEmpty()) {
            HashMap<Literal, Literal> qPrime = new HashMap<Literal, Literal>();

            HashSet<Predicate> positive = getPositive(q);
            HashSet<Predicate> negative = getNegative(q);
          //  System.out.println();
           // System.out.println("ALL positive: " + positive);

           // System.out.println("ALL negative: " + negative);
            
            for (Literal lit : q.values()) {
                HashSet<Action> actions = lit
                        .actionsWherePrecondition(Program3.groundedActions);

                for (Action a : actions) {
                    //System.out.println("adding aciton: " + a);
                    if (a.possible(positive, negative)) {
                        for (Predicate p : a.getDel()) {
                            Literal newLit = new Literal(time + 1, p, false);

                            if (!negative.contains(p)) {

                             //   System.out.println("adding false predicate: " + p);
                                qPrime.put(newLit, newLit);
                            }
                        }

                        for (Predicate p : a.getAdd()) {
                            Literal newLit = new Literal(time + 1, p, true);

                            if (!positive.contains(p)) {
                              //  System.out.println("adding true predicate: " + p);
                                qPrime.put(newLit, newLit);
                            }
                        }
                    }
                }
            }
            
            time = time + 1;
            q = qPrime;
            //q.putAll(qPrime);            
        }

        return q;
    }

    private static double maxTime(HashMap<Literal, Literal> queue,
            HashSet<Predicate> goal, HashSet<Predicate> goalNeg) {
        double max = Double.MIN_VALUE;

        if (queue.isEmpty()) {
            return Double.MAX_VALUE;
        }

        for (Predicate p : goal) {
            Literal tempLit = new Literal(0, p, true);
            Literal lit = queue.get(tempLit);
            int t = lit.getTimeBecameTrue();

            if (max < t) {
                max = t;
            }
        }

        for (Predicate p : goalNeg) {
            Literal tempLit = new Literal(0, p, false);
            Literal lit = queue.get(tempLit);
            int t = lit.getTimeBecameTrue();

            if (max < t) {
                max = t;
            }
        }

        return max;
    }

    private static double sumTimes(HashMap<Literal, Literal> queue,
            HashSet<Predicate> goal, HashSet<Predicate> goalNeg) {
        double sum = 0.0;

        if (queue.isEmpty()) {
            return Double.MAX_VALUE;
        }

        for (Predicate p : goal) {
            Literal tempLit = new Literal(0, p, true);
            Literal lit = queue.get(tempLit);

            sum = sum + lit.getTimeBecameTrue();
        }

        for (Predicate p : goalNeg) {
            Literal tempLit = new Literal(0, p, false);
            Literal lit = queue.get(tempLit);

            sum = sum + lit.getTimeBecameTrue();
        }

        return sum;
    }

    private static boolean allGoalsTrue(HashMap<Literal, Literal> queue,
            HashSet<Predicate> goal, HashSet<Predicate> goalNeg) {
        for (Predicate p : goal) {
            Literal tempLit = new Literal(0, p, true);

            if (!queue.containsKey(tempLit)) {
             //   System.out.println("true missing :" + p);
                return false;
            }
        }

        for (Predicate p : goalNeg) {
            Literal tempLit = new Literal(0, p, false);

            if (!queue.containsKey(tempLit)) {
               // System.out.println("false missing :" + p);
                return false;
            }
        }

        return true;
    }

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

    private static HashMap<Literal, Literal> getInitialQueue(State state) {
        HashMap<Literal, Literal> literals = new HashMap<Literal, Literal>();

        for (Predicate p : state.getPositive()) {
            Literal lit = new Literal(0, p, true);

            literals.put(lit, lit);
        }

        // ////// ??????
        for (Predicate p : state.getNegative()) {
            Literal lit = new Literal(0, p, false);

            literals.put(lit, lit);
        }

        return literals;
    }

    private static class Literal {
        private final int timeBecameTrue;
        private final Predicate predicate;
        private final boolean positive;

        public Literal(int timeBecameTrue, Predicate predicate, boolean positive) {
            this.timeBecameTrue = timeBecameTrue;
            this.predicate = predicate;
            this.positive = positive;
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

        public HashSet<Action> actionsWherePrecondition(
                HashSet<Action> allActions) {
            if (positive) {
                return predicate.actionsWherePrecondition(allActions);
            }

            return predicate.actionsWherePrenegcondition(allActions);
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