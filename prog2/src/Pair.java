import java.util.UUID;

/**
 * Represents a pair of Clauses that have been previously resolved with each
 * other.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Pair {	
	private UUID a;
	private UUID b;
	
	/**
	 * Creates a Pair from two clauses.
	 * @param clauseA The first Clause.
	 * @param clauseB The second Clause.
	 */
	public Pair(Clause clauseA, Clause clauseB) {
		if (clauseA.getId().compareTo(clauseB.getId()) < 0) {
			this.a = clauseA.getId();
			this.b = clauseB.getId();
		} else {
			this.a = clauseB.getId();
			this.b = clauseA.getId();
		}	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
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
		Pair other = (Pair) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}
}
