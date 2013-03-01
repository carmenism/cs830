
public class Program2 {
	public Program2() {
		KnowledgeBase kb = Parser.getKBFromStandardIn();
		kb.resolve();
	}
	
	public static void main(String [] args) {
		new Program2();
	}
}
