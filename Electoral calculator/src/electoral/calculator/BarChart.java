package electoral.calculator;

import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset; 

public class BarChart {
	
	static JFrame frame= new JFrame();
	
	public BarChart(String[] partyVoteCounts){
		
		String title="Vote bar chart";
		String chartTitle="Vote bar chart";
		frame.removeAll();
		frame.revalidate();
		frame= new JFrame();
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		ArrayList<Candidate> list= ElectoralCalculator.getCandidateList();
		
		
		

		
		JFreeChart barChart = ChartFactory.createBarChart(
		         chartTitle,           
		         "Candidates",            
		         "Votes",            
		         createDataset(list,partyVoteCounts),          
		         PlotOrientation.VERTICAL,           
		         true, true, false);
		         
		barChart.removeLegend();
		CategoryPlot plot = (CategoryPlot) barChart.getPlot();
		
		ValueAxis yAxis = plot.getRangeAxis();
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		CategoryAxis xAxis = plot.getDomainAxis();
	    xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		
	    ChartPanel chartPanel = new ChartPanel( barChart );        
	    chartPanel.setPreferredSize(new java.awt.Dimension( 1200 , 800 ) );        
	      //setContentPane( chartPanel ); 
	    frame.setSize(1200, 800);
	    frame.setLocationRelativeTo(null);
	    frame.setResizable(false);
	    frame.add(chartPanel);
	    frame.setVisible(true);
	    
	    
	    
	}
	
	 private CategoryDataset createDataset(ArrayList<Candidate> candidateList, String[] partyResults ) {
		 DefaultCategoryDataset dataset = new DefaultCategoryDataset( );  
		 
		 for(Candidate c: candidateList){
	    	  dataset.addValue((double)c.getVoteCount(), "Votes" , c.getFirstName()+" "+c.getLastName());
	      }
	      	dataset.addValue(0.0, "Votes", "");
	      	
	      	      
	      for(int i=0; i<partyResults.length-1; i++){
	    	  
	    	  String[] tokens= partyResults[i].split(" ");
	    	  String partyName="";
	    	  for(int j=0; j< tokens.length-1; j++){
	    		  partyName+=tokens[j]+" ";
	    	  }
	    	  dataset.addValue(Integer.parseInt(tokens[tokens.length-1]), "Votes", partyName );
	    	  
	      }
	      String[] tokens= partyResults[partyResults.length-1].split(" ");
	      dataset.addValue(Integer.parseInt(tokens[2]), "Votes", "Invalid/Blank votes");
	      return dataset; 
	 }

}
