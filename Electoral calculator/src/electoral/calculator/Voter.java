package electoral.calculator;

public class Voter extends Person{
	
	private String pesel;

	
	public Voter (String firstName, String lastName, String pesel){
		
		super(firstName, lastName);
		setPesel(pesel);	
		
	}


	public String getPesel() {
		return pesel;
	}


	public void setPesel(String pesel) {
		this.pesel = pesel;
	}
	
}
