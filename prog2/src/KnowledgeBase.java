import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Represents a knowledge base of first-order logic in conjunctive normal form.
 * 
 * @author Carmen St. Jean
 * 
 */
public class KnowledgeBase {
    private final List<Clause> input;
    private final List<Clause> setOfSupport;

    private boolean lookForEmpty;

    private List<Clause> solution;

    private int numberResolved = 0;

    /**
     * Creates a KnowledgeBase from a collection of Clauses.
     * 
     * @param input
     *            The input Clauses.
     * @param setOfSupport
     *            The set of support Clauses.
     */
    public KnowledgeBase(List<Clause> input, List<Clause> setOfSupport) {
        this.input = input;
        this.setOfSupport = setOfSupport;
        this.solution = new ArrayList<Clause>();

        for (int i = 0; i < input.size(); i++) {
            input.get(i).setNumber(i + 1);
        }

        this.lookForEmpty = true;

        for (int i = 0; i < setOfSupport.size(); i++) {
            Clause clause = setOfSupport.get(i);
            clause.setNumber(i + 1 + input.size());

            if (clause.containsAns()) {
                this.lookForEmpty = false;
            }
        }
    }

    /**
     * Prints the original input.
     */
    private void printInput() {
        for (Clause clause : input) {
            System.out.println(clause.getNumber() + ": " + clause);
        }
    }

    /**
     * Prints the original set of support.
     */
    private void printSetOfSupport() {
        for (Clause clause : setOfSupport) {
            System.out.println(clause.getNumber() + ": " + clause);
        }
    }

    /**
     * Prints the number of resolutions.
     */
    private void printNumberResolutions() {
        System.out.println(numberResolved + " total resolutions");
    }

    /**
     * Determines if the specified Clause is the goal Clause.
     * 
     * @param clause
     *            The Clause to check.
     * @return True if it is the goal.
     */
    public boolean isGoal(Clause clause) {
        if (lookForEmpty) {
            return clause.isEmpty();
        }

        return clause.isAns();
    }

    /**
     * Attempts to resolve the knowledge base.
     * 
     * @return True if the knowledge base is provable. False if the knowledge
     *         base is unprovable.
     */
    public boolean resolve() {
        PriorityQueue<Clause> sos = new PriorityQueue<Clause>(setOfSupport);
        List<Clause> sosGrave = new ArrayList<Clause>();
        HashSet<Pair> pairs = new HashSet<Pair>();
        HashSet<String> clausesProduced = new HashSet<String>();

        for (Clause i : input) {
            clausesProduced.add(i.toString());
        }
        for (Clause s : setOfSupport) {
            clausesProduced.add(s.toString());
        }

        while (!sos.isEmpty()) {
            Clause smallest = sos.poll();

            PriorityQueue<Clause> others = getOtherClauses(sos, sosGrave);

            while (!others.isEmpty()) {
                Clause smallestOther = others.poll();
                Pair pair = new Pair(smallest, smallestOther);

                if (!pairs.contains(pair)) {
                    Clause resolved = smallest.resolve(smallestOther);

                    if (resolved != null) {
                        numberResolved++;

                        if (isGoal(resolved)) {
                            printSolution(resolved);

                            return true;
                        } else if (!clausesProduced.contains(resolved
                                .toString())) {
                            clausesProduced.add(resolved.toString());
                            sos.add(resolved);
                        }
                    }

                    pairs.add(pair);
                }
            }

            sosGrave.add(smallest);
        }

        printInput();
        printSetOfSupport();
        System.out.println("No proof exists.");
        printNumberResolutions();

        return false;
    }

    /**
     * Builds a PriorityQueue of other Clauses to compare with the current smallest Clause. 
     * @param sos The set of support clauses.
     * @param sosGrave The set of support clauses that have already been compared.
     * @return A PriorityQueue of Clauses for comparison.
     */
    private PriorityQueue<Clause> getOtherClauses(PriorityQueue<Clause> sos,
            List<Clause> sosGrave) {
        PriorityQueue<Clause> others = new PriorityQueue<Clause>();

        others.addAll(sos);
        //others.addAll(sosGrave);
        others.addAll(input);

        return others;
    }

    /**
     * Prints the solution.
     * 
     * @param goalClause
     *            The goal clause that was found.
     */
    private void printSolution(Clause goalClause) {
        buildSolution(goalClause);

        int num = input.size() + setOfSupport.size() + 1;

        for (int i = solution.size() - 1; i >= 0; i--) {
            solution.get(i).setNumber(num);
            num++;
        }

        printInput();
        printSetOfSupport();

        for (int i = solution.size() - 1; i >= 0; i--) {
            Clause clause = solution.get(i);
            int c = clause.getNumber();

            int a = clause.getParentA().getNumber();
            int b = clause.getParentB().getNumber();

            // Make sure the larger number comes first.
            if (a < b) {
                int sub = a;
                a = b;
                b = sub;
            }

            System.out.println(a + " and " + b + " give " + c + ": " + clause);
        }

        printNumberResolutions();
    }

    /**
     * Recursively builds the solution from the given clauses.
     * 
     * @param clause
     *            The clause to attempt to add to the solution.
     */
    private void buildSolution(Clause clause) {
        if (clause.getParentA() != null || clause.getParentB() != null) {
            solution.add(clause);

            if (clause.getParentA() != null) {
                buildSolution(clause.getParentA());
            }

            if (clause.getParentA() != null) {
                buildSolution(clause.getParentB());
            }
        }
    }
}
