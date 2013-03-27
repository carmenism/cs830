import java.util.List;
import java.util.ArrayList;

/**
 * Represents a collection of list of constants where all lists are of the same
 * length. Each list is a unique permutation of constants. This is a
 * representation of the external direct product of the constants.
 * 
 * @author Carmen St. Jean
 * 
 */
public class ConstantSet extends ArrayList<List<Constant>> {
    private static final long serialVersionUID = 1L;

    private final Integer length;

    /**
     * Creates a new list of all possible constant permutations.
     * 
     * @param length
     *            A length signifying how long each list of constants should be.
     * @param allConstants
     *            A list of all possible constants.
     */
    public ConstantSet(int length, List<Constant> allConstants) {
        this.length = length;

        int n = allConstants.size();

        for (int r = 0; r < Math.pow(n, length); r++) {
            List<Constant> list = new ArrayList<Constant>();

            for (int c = 0; c < length; c++) {
                int i = (r / (int) Math.pow(n, c)) % n;

                list.add(allConstants.get(i));
            }

            super.add(list);
        }
    }

    public Integer getLength() {
        return length;
    }
}
