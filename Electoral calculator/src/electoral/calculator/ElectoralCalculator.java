package electoral.calculator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;

public class ElectoralCalculator {
	
	private static ArrayList<Candidate> candidateList= new ArrayList<>();
	
	public static void main(String[] args){
		new ElectoralCalculator();
				
		
	}
	
	public ElectoralCalculator(){
		
		initializeCandidateList();
		
	}
	
	

	private static void  initializeCandidateList() {
		candidateList= new ArrayList<>();
		String urlString="http://webtask.future-processing.com:8069/candidates";
		String[] candidates = NetClient.getXMLCandidates(NetClient.getDocument(urlString));
		
		for(int i=0; i<candidates.length; i+=2){
			String[] tokens= candidates[i].split(" ");
			String firstName= tokens[0];
			String lastName= tokens[1];
			String party= candidates[i+1];
			
			addCandidateToList(firstName,lastName,party);
		}
		
	}
	
	public static ArrayList<Candidate> getCandidateList(){
		return candidateList;
	}
	
	private static void  addCandidateToList(String firstName, String lastName, String party){
		candidateList.add(new Candidate(firstName,lastName,party));
	}
	
	public static boolean isValidName(String name){
		
		Pattern p = Pattern.compile("^[A-Za-zzżźćńółęąśŻŹĆĄŚĘŁÓŃ\\s]{1,30}$");
		Matcher m= p.matcher(name);
		return m.matches();
		
	}	

	public static boolean isValidVote(JCheckBox[] checkBoxes){
		int selectedCheckBoxes=0;

		for(JCheckBox checkBox: checkBoxes){
			
			if(checkBox.isSelected()){
				selectedCheckBoxes++;
			}
		}
		
		if(selectedCheckBoxes==1){
			return true;
		}else{
			return false;
		}
	}
	
	public static int candidateIndex(JCheckBox[] checkBoxes){
			
		int selectedIndex=0;
		int index=0;
		
		for(JCheckBox checkBox: checkBoxes){

			if(checkBox.isSelected()){
				selectedIndex=index;
			}
			index++;
		}
		return selectedIndex;
	}
	
	
	

}
