package electoral.calculator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class SSHDBConnection {
	
   static Session session= null;
	
	
	private static Connection getDBConnection(){
		
		Connection connection = null;

        
		String host = "151.80.148.124";
		String servUser = "elections";
		String servPwd = "kornel";
		int port = 22;
		
		String rhost = "localhost";
		int rport = 3306;
		int lport = 3307;

		String driverName = "com.mysql.jdbc.Driver";
		String db2Url = "jdbc:mysql://localhost:" + lport + "/elections?autoReconnect=true&useSSL=false";
		String dbUsr = "elections";
		String dbPwd = "elect";
		
		try {
			JSch jsch = new JSch();

			session = jsch.getSession(servUser, host, port);
			session.setPassword(servPwd);
			
			java.util.Properties config = new java.util.Properties();

			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			session.connect();

			session.setPortForwardingL(lport, rhost, rport);

			Class.forName(driverName);
			
			connection = DriverManager.getConnection(db2Url, dbUsr, dbPwd);
			
			
			return connection;
			
			
		}catch(SQLException e){
			
			JOptionPane.showMessageDialog(null, "Something went wrong, please try again");
			
		} catch (JSchException e) {
			
			JOptionPane.showMessageDialog(null, "Something went wrong, please try again");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return connection;
		
	}
	
		public static void saveVoterInDB(Voter v, boolean isValidVote, int candidateIndex){
			String validVote;
			int index=0;
			if(isValidVote){
				 index=candidateIndex+1;
				 validVote="TRUE";
			}else{
				 validVote="FALSE";
			}
			
			try{
			Connection connection= getDBConnection();
			Statement sqlState = connection.createStatement();
			String insertVoter="INSERT INTO voter VALUES "
					+ "('"+Encryption.encrypt(v.getPesel())
					+"','"+Encryption.encrypt(v.getFirstName())
					+"','"+Encryption.encrypt(v.getLastName())					
					+"',"+validVote+","+index+")"
					+" ON DUPLICATE KEY UPDATE firstName=firstName";
			

			sqlState.executeUpdate(insertVoter);
			
			if(isValidVote){
			String addVote="UPDATE candidates"+
			" SET voteCount = voteCount + 1 "+
			"WHERE id="+index;

			sqlState.executeUpdate(addVote);
			}
			connection.close();
			session.disconnect();
			}catch(SQLException e){
				//connection.close();
				session.disconnect();
				JOptionPane.showMessageDialog(null, "Something went wrong, please try again");
			}
			
		}
		
		public static String[] getVoteCounts(ArrayList<Candidate> candidateList){
			
			int numberOfPartys=4;
			String[] results= new String[numberOfPartys+1];
			try{
				Connection connection= getDBConnection();
				Statement sqlState= connection.createStatement();
				String statement="SELECT * FROM candidates";
				
				
				ResultSet rows = sqlState.executeQuery(statement);
				int index=0;
				while(rows.next()){

					candidateList.get(index).setVoteCount(rows.getInt("voteCount"));
					index++;
				}
				

				statement="SELECT party,"
						+" SUM(voteCount) as voteCount"
						+" FROM candidates"
						+" GROUP BY party";
				rows = sqlState.executeQuery(statement);
				
				index=0;
				while(rows.next()){
					results[index]=rows.getString("party")+ " " +rows.getString("voteCount");
					index++;
				}

				
				statement="SELECT COUNT(validVote) as 'Invalid vote'"
						+" FROM voter"
						+" WHERE validVote=0";
				rows = sqlState.executeQuery(statement);
				
				while(rows.next()){
					results[4]="There was "+rows.getString("Invalid Vote")+" invalid votes";
				}
				
				connection.close();
				session.disconnect();

				
				}catch(SQLException e){
					JOptionPane.showMessageDialog(null, "Something went wrong, please try again");
					session.disconnect();
				}
			
			return results;
			
		}
		
		public static void saveFailedAttempt(Voter v){

			try{
				Connection connection= getDBConnection();
				Statement sqlState = connection.createStatement();
				String insertVoter="INSERT INTO failedTries VALUES "
						+ "('"+Encryption.encrypt(v.getPesel())
						+"','"+Encryption.encrypt(v.getFirstName())
						+"','"+Encryption.encrypt(v.getLastName())+"')"
						+" ON DUPLICATE KEY UPDATE firstName=firstName";					
						
				

				sqlState.executeUpdate(insertVoter);
				

				connection.close();
				session.disconnect();

				
				}catch(SQLException e){
					

						session.disconnect();
	
				}catch(Exception e){
					
					JOptionPane.showMessageDialog(null, "Something went wrong, please try again");
					session.disconnect();
				}
			
			
		}
		
		public static int getFailedAttempts(){
			
			int uniqueFailedAttempts=0;
			try{

				Connection connection= getDBConnection();
				Statement sqlState = connection.createStatement();
				String getFailed="SELECT pesel from failedTries";			
				int index=0;

				ResultSet rows = sqlState.executeQuery(getFailed);
				while(rows.next()){
					index++;
				}
				
				uniqueFailedAttempts=index-1;

				
				connection.close();
				session.disconnect();

				return uniqueFailedAttempts;
				}catch(SQLException e){

					JOptionPane.showMessageDialog(null, "Something went wrong, please try again");
						session.disconnect();
	
				}catch(Exception e){
					

					session.disconnect();
				}
			
			return uniqueFailedAttempts;
			
		}
		
		public static boolean isVoterInDB(Voter v){
			String encryptedPesel=Encryption.encrypt(v.getPesel());
			
			try{
			Connection connection= getDBConnection();
			Statement sqlState= connection.createStatement();
			String statement="Select pesel from voter";
			
			ResultSet rows = sqlState.executeQuery(statement);
			
			while(rows.next()){
				String row=rows.getString("pesel");
				if(row.equals(encryptedPesel)){
					connection.close();
					session.disconnect();
					return true;
				}
			}
			connection.close();
			session.disconnect();
			
			
			}catch(SQLException e){
				JOptionPane.showMessageDialog(null, "Something went wrong, please try again");
			}

			return false;
		}

}
