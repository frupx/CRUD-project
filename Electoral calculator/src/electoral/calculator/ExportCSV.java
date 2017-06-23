package electoral.calculator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ExportCSV {
	
	 private static final char DEFAULT_SEPARATOR = ',';

	 public static void writeLine(Writer w, List<String> values) throws IOException {
	        writeLine(w, values, DEFAULT_SEPARATOR, ' ');
	 }

	 public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
	        writeLine(w, values, separators, ' ');
	 }
	    
	    
	 private static String followCVSformat(String value) {

	        String result = value;
	        if (result.contains("\"")) {
	            result = result.replace("\"", "\"\"");
	        }
	        return result;

	}
	    
	public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

	        boolean first = true;

	        //default customQuote is empty

	        if (separators == ' ') {
	            separators = DEFAULT_SEPARATOR;
	        }

	        StringBuilder sb = new StringBuilder();
	        for (String value : values) {
	            if (!first) {
	                sb.append(separators);
	            }
	            if (customQuote == ' ') {
	                sb.append(followCVSformat(value));
	            } else {
	                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
	            }

	            first = false;
	        }
	        sb.append("\n");
	        w.append(sb.toString());


	}

	public static void export(ArrayList<Candidate> candidateList, String[] partyResults,
			int failedAttempts) {
		
		try{
			final JFileChooser fc= new JFileChooser();
			int returnVal= fc.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String filePath= file.toString();

				if(!filePath.substring(filePath.length()-5, filePath.length()-1).equals(".csv")){
					filePath+=".csv";
				}

			
		FileWriter writer= new FileWriter(filePath);
		
	    int validVotes=0;
		
	    for(Candidate c :candidateList){

	    	List<String> list=new ArrayList<>();
	    	list.add(c.getFirstName());
	    	list.add(c.getLastName());
	    	list.add(c.getPartyName());
	    	list.add(String.valueOf(c.getVoteCount()));
	    	writeLine(writer,list);
	    	validVotes+=c.getVoteCount();
	    	    
	    }
	    

	    
	    for(int i=0; i<partyResults.length-1; i++){
	    	  
	    	  String[] tokens= partyResults[i].split(" ");
	    	  List<String> list= new ArrayList<>();
	    	  String partyName="";
	    	  for(int j=0; j< tokens.length-1; j++){
	    		  if(j!=tokens.length-2){
	    		  partyName+=tokens[j]+" ";
	    		  }else{
	    			  partyName+=tokens[j];
	    		  }
	    			  

	    	  }
    		  list.add(partyName);
    		  list.add(tokens[tokens.length-1]);

	    	  writeLine(writer,list);
	    }
	    
	    List<String> list= new ArrayList<>();
	    
	    
	    //writing Invalid and blank votes
	    list.add("Invalid/Blank votes");

	    String[] tokens= partyResults[partyResults.length-1].split(" ");
	    list.add(tokens[2]);	    
	    writeLine(writer,list);
	    
	    list= new ArrayList<>();
	    
	    list.add("Valid votes");
	    list.add(String.valueOf(validVotes));
	    
	    writeLine(writer,list);
	    
	    list= new ArrayList<>();
	    
	    list.add("Failed attempts");
	    list.add(String.valueOf(failedAttempts));
	    
	    
		writer.flush();
    	writer.close();
    	
    	JOptionPane.showMessageDialog(null, "CSV file saved.");
			}
		}catch (IOException e){
			JOptionPane.showMessageDialog(null, "Something went wrong, please try again");
		}

		
	}

}
