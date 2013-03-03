/**
 * CS 830 - Artificial Intelligence - Program 2
 * 
 * First-order logic theorem prover.  Reads input from standard input like below:
 * 
 *      -Human(x1) | Mortal(x1)
 *      Human(Socrates)
 *      Animal(F2(x2)) | Loves(F1(x2), x2)
 *      -Loves(x2, F2(x2)) | Loves(F1(x2), x2)
 *      --- negated query ---
 *      -Mortal(Socrates)
 * 
 * @author Carmen St. Jean
 *
 */
public class Program2 {
	public Program2() {
		KnowledgeBase kb = Parser.getKBFromStandardIn();
		kb.resolve();
	}
	
	public static void main(String [] args) {
		new Program2();
	}
}