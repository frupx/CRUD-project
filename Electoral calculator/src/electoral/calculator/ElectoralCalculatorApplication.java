package electoral.calculator;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.itextpdf.text.DocumentException;

public class ElectoralCalculatorApplication {
	
	static JLabel firstNameLabel, lastNameLabel, peselLabel;
	static JTextField firstNameTF, lastNameTF, peselTF;
	
	static JButton logInButton;
	
	static String windowTitle= "Electoral Application";
	
	
	static JCheckBox[] candidateCheckBoxes;
	
	static JFrame logInFrame;
	public static JFrame voteFrame;
	
	static JButton voteButton, logOutButton, createBarChartButton, exportCSVButton, exportPDFButton;
	static Voter voter;
	

	
	
	
	public static void main (String[] args){
		
		logInWindow();
		
		
	}
	
	
	public static void logInWindow(){		
		 
		
		logInFrame= new JFrame();
		logInFrame.revalidate();
		logInFrame.setTitle(windowTitle);
		logInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		logInFrame.setResizable(false);
		
		firstNameLabel= new JLabel("First Name:");
		lastNameLabel= new JLabel("Last name:");
		peselLabel= new JLabel("PESEL number:");
		
		firstNameTF= new JTextField();
		lastNameTF= new JTextField();
		peselTF= new JTextField();
		
		logInButton= new JButton("Log in");
		ListenForButton lForButton= new ListenForButton();
		
		logInButton.addActionListener(lForButton);
		
		Box box= Box.createVerticalBox();
		
		box.add(firstNameLabel);
		box.add(firstNameTF);
		
		box.add(lastNameLabel);
		box.add(lastNameTF);
		
		box.add(peselLabel);
		box.add(peselTF);
		
		logInFrame.add(logInButton,BorderLayout.SOUTH);
		
		logInFrame.add(box);
		logInFrame.setSize(400,200);
		logInFrame.setLocationRelativeTo(null);
		logInFrame.setVisible(true);
	} //end of logInWindow
	
	public static void voteWindow(){
		
		voteFrame= new JFrame();
		voteFrame.revalidate();
		voteFrame.setTitle(windowTitle);
		voteFrame.setResizable(false);
		voteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		new ElectoralCalculator();		//constructor initializes list of candidates
		
		candidateCheckBoxes= makeCheckBoxes();
		
		Box candidateBox= fillCandidateBox(candidateCheckBoxes);
		
		voteButton= new JButton("Vote");
		logOutButton = new JButton("Log out");
		createBarChartButton=new JButton("Show bar chart");
		exportPDFButton= new JButton("Export to PDF");
		exportCSVButton= new JButton("Export to CSV");
		
		ListenForButton lForButton= new ListenForButton();
		voteButton.addActionListener(lForButton);
		logOutButton.addActionListener(lForButton);
		createBarChartButton.addActionListener(lForButton);
		exportPDFButton.addActionListener(lForButton);
		exportCSVButton.addActionListener(lForButton);

		
		
		Box buttonBox= Box.createHorizontalBox();
		buttonBox.add(voteButton);
		buttonBox.add(logOutButton);
		buttonBox.add(createBarChartButton);
		buttonBox.add(exportPDFButton);
		buttonBox.add(exportCSVButton);
		
		voteFrame.add(buttonBox, BorderLayout.CENTER);
		voteFrame.add(candidateBox, BorderLayout.NORTH);
		
		voteFrame.setLocationRelativeTo(null);
		voteFrame.pack();

		voteFrame.setVisible(true);
		
				
	}	//END OF voteWindow
	
	
	
	private static Box fillCandidateBox(JCheckBox[] candidateCheckBoxes) {
		
		Box returnBox= Box.createVerticalBox();
		int arrayLength= candidateCheckBoxes.length;
		
		for(int i=0; i<arrayLength; i++){
			returnBox.add(candidateCheckBoxes[i]);
		}
		
		
		return returnBox;
	}


	private static JCheckBox[] makeCheckBoxes(){
		
		
		ArrayList<Candidate> tempList= ElectoralCalculator.getCandidateList();
		int numberOfCandidates=tempList.size();

		JCheckBox[] returnCheckBoxes= new JCheckBox[numberOfCandidates];
		for(int i=0; i<numberOfCandidates; i++){
			Candidate c=tempList.get(i);
			String candidate= c.getFirstName()+ " " + c.getLastName();
			returnCheckBoxes[i]=new JCheckBox(candidate);
		}
		
		return returnCheckBoxes;
			
	}
	
	
	private static class ListenForButton implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==logInButton){				

				String firstName= firstNameTF.getText();
				String lastName= lastNameTF.getText();
				String pesel= peselTF.getText();
				
				String response=checkData(firstName,lastName,pesel);
				voter= new Voter(firstName, lastName, pesel);
				if(response.equals("")){
					
					logInFrame.dispose();
					voteWindow();
					
				}else  if(ElectoralCalculator.isValidName(firstName)&&ElectoralCalculator.isValidName(lastName)&&
						(PeselChecker.isPeselBlackListed(pesel)||!PeselChecker.isAnAdult(pesel))){
						SSHDBConnection.saveFailedAttempt(voter);
						JOptionPane.showMessageDialog(null, response);
				
				}else{
					JOptionPane.showMessageDialog(null, response);
				}
				
				
			}else if(e.getSource()==logOutButton){
				String confirmDialog="Are you sure you want to log out?";
				int dialogResult = JOptionPane.showConfirmDialog (null, confirmDialog,"Warning",2);
				if(dialogResult == JOptionPane.YES_OPTION){
				 voteFrame.dispose();
				 logInWindow();
				}
				
				
				
			}else if(e.getSource()==voteButton){
				String confirmDialog="Are you sure you want to save your vote? You cannot change it later.";
				int dialogResult = JOptionPane.showConfirmDialog (null, confirmDialog,"Alert",2);
				if(dialogResult == JOptionPane.YES_OPTION){
				
				 boolean validVote=ElectoralCalculator.isValidVote(candidateCheckBoxes);
				 int candidateIndex=ElectoralCalculator.candidateIndex(candidateCheckBoxes);
				 if(!SSHDBConnection.isVoterInDB(voter)){
						
						SSHDBConnection.saveVoterInDB(voter,validVote,candidateIndex);
						JOptionPane.showMessageDialog(null, "Thanks you for voting");
					}else{
						JOptionPane.showMessageDialog(null, "You have already voted!");
					}
				String[] partyVoteCounts= SSHDBConnection.getVoteCounts(ElectoralCalculator.getCandidateList());
				displayResults(partyVoteCounts);
				}
		
			
			}else if(e.getSource()==createBarChartButton){
				String[] partyVoteCounts= SSHDBConnection.getVoteCounts(ElectoralCalculator.getCandidateList());
				new BarChart(partyVoteCounts);
			
			
			}else if(e.getSource()==exportPDFButton){
				
				
				String[] partyVoteCounts= SSHDBConnection.getVoteCounts(ElectoralCalculator.getCandidateList());
				try {
					ExportPDF.createPdf(ElectoralCalculator.getCandidateList(), partyVoteCounts, SSHDBConnection.getFailedAttempts());
				} catch (IOException | DocumentException e1) {
					JOptionPane.showMessageDialog(null, "This file is already open. Choose another file or close this one.");
				}
			
			
			}else if (e.getSource()==exportCSVButton){
				
				String[] partyVoteCounts= SSHDBConnection.getVoteCounts(ElectoralCalculator.getCandidateList());
				ExportCSV.export(ElectoralCalculator.getCandidateList(), partyVoteCounts, SSHDBConnection.getFailedAttempts());
				
			}
			

		
			
		}
		
	}
	
	public static void displayResults(String[] partyVoteCounts){
		

		String results="Current vote count for candidates: \n";
		ArrayList<Candidate> candidateList= ElectoralCalculator.getCandidateList();
		
		for(Candidate c: candidateList){
			results+=c.getFirstName()+"   "+c.getLastName()+"        votes:    "+c.getVoteCount()+"\n";
		}
		results+="\n\nCurrent vote count for parties:\n";
		for(String s:partyVoteCounts){
			results+=s+"\n";
		}

		JOptionPane.showMessageDialog(null, results);


		
		
	}
	
	public static String checkData(String firstName, String lastName, String pesel){
		String response="";
		if(!ElectoralCalculator.isValidName(firstName)){
			response+="The first name you entered is invalid\n";
		}
		if(!ElectoralCalculator.isValidName(lastName)){
			response+="The last name you entered is invalid\n";
		}

		if(!PeselChecker.isValidPesel(pesel)){
			response+="The PESEL number you entered is invalid\n";
		}else{
			
			if(PeselChecker.isPeselBlackListed(pesel)){
				response+="The PESEL number you entered is black listed. You can't log in.";					
			}
			if(!PeselChecker.isAnAdult(pesel)){
				response+="You are too young to vote";
			}
		}
			
		

		
		return response;
	}

} //end of ElectoralCalculatorApplication class
