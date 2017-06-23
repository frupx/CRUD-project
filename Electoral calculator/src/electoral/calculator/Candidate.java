package electoral.calculator;

public class Candidate extends Person{
	
	private String partyName;
	private int voteCount;
	
	
	public Candidate(String firstName, String lastName, String partyName){
		super(firstName,lastName);
		setPartyName(partyName);
		
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

}
