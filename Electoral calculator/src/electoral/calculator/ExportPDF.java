package electoral.calculator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class ExportPDF {
	
	 static BaseFont helvetica;
	
	public static void createPdf(ArrayList<Candidate> candidateList, String[] partyResults, int failedAttempts) throws IOException, DocumentException {

		
		
		
	    Document document = new Document(PageSize.A4.rotate());
	    String filePath="";
	    final JFileChooser fc= new JFileChooser();
		int returnVal= fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			filePath= file.toString();
			
			
			if(!filePath.substring(filePath.length()-5, filePath.length()-1).equals("pdf")){
				filePath+=".pdf";
			}
			
		}
		
	    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
	    writer.setPdfVersion(PdfWriter.VERSION_1_7);

	    document.open();

	    
	    
	    document.add(createTable( candidateList, partyResults,  failedAttempts));
	 
	    JOptionPane.showMessageDialog(null, "PDF file saved");
	    document.close();
	}
	
	
	public static PdfPTable createTable(ArrayList<Candidate> candidateList, String[] partyResults, int failedAttempts) {
    	// a table with three columns
        PdfPTable table = new PdfPTable(3);
        
 
       	try {
			helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       	
       	
    	com.itextpdf.text.Font polishFont=new com.itextpdf.text.Font(helvetica,10);
        table.addCell("Candidate");
        table.addCell("Party");
        table.addCell("Vote count");
        
        int validVotes=0;
        
	    for(Candidate c :candidateList){
		    PdfPCell  cell= new PdfPCell (new Phrase(c.getFirstName()+ " " + c.getLastName(),polishFont));
	    	table.addCell(cell);
	    	cell= new PdfPCell (new Phrase(c.getPartyName(),polishFont));
	    	table.addCell(cell);
	    	cell= new PdfPCell (new Phrase(String.valueOf(c.getVoteCount()),polishFont));
	    	table.addCell(cell);
	    	
	    	validVotes+=c.getVoteCount();
	    }
	    
	    PdfPCell cell= new PdfPCell(new Phrase(" "));
	    cell.setColspan(3);
	    table.addCell(cell);
	    
	      cell= new PdfPCell (new Phrase("Party name",polishFont));
	    cell.setColspan(2);
	    
	    table.addCell(cell);
	    table.addCell("Vote count");
	    
	    for(int i=0; i<partyResults.length-1; i++){
	    	  
	    	  String[] tokens= partyResults[i].split(" ");
	    	  String partyName="";
	    	  for(int j=0; j< tokens.length-1; j++){
	    		  partyName+=tokens[j]+" ";
	    	  }
	    	
	    	  cell= new PdfPCell (new Phrase(partyName,polishFont));
	  	    cell.setColspan(2);
	  	    table.addCell(cell);
	    	table.addCell(tokens[tokens.length-1]);
	    }
	    
	   
	    
	    String tokens[]= partyResults[partyResults.length-1].split(" ");
	    int invalidVotes= Integer.parseInt(tokens[2]);
	    
	    cell= new PdfPCell(new Phrase(" "));
	    cell.setColspan(3);
	    table.addCell(cell);
	    
	    cell= new PdfPCell (new Phrase("Invalid/ Blank votes"));
	    cell.setColspan(2);
	    table.addCell(cell);
	    
	    table.addCell(String.valueOf(invalidVotes));
	    
	    cell= new PdfPCell (new Phrase("Valid votes"));
	    cell.setColspan(2);
	    table.addCell(cell);
	    
	    table.addCell(String.valueOf(validVotes));
	    
	    cell= new PdfPCell (new Phrase("Failed attempts (too young or blacklisted)"));
	    cell.setColspan(2);
	    table.addCell(cell);
	    
	    table.addCell(String.valueOf(failedAttempts));
	    
		LocalDateTime now = LocalDateTime.now();
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
		cell= new PdfPCell(new Phrase("PDF generated: "+ now.format(formatter)));
		cell.setColspan(3);
		table.addCell(cell);
	    
        return table;
    }
	

}
