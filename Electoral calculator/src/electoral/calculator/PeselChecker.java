package electoral.calculator;

import java.time.LocalDate;
import java.time.Period;

public class PeselChecker {
	
	
	public static boolean isAnAdult(String pesel){
		int[] peselNumbers= new int[11];
		
		if(pesel.length() !=11){
			return false;
		}else{
			for(int i=0; i< 11; i++){
				String temp=""+pesel.charAt(i);
				peselNumbers[i]= Integer.parseInt(temp);
			}
	}
		int day=10*peselNumbers[4]+peselNumbers[5];
		int year=getYear(peselNumbers);
		int month=getMonth(peselNumbers);
		
		LocalDate today = LocalDate.now();
		LocalDate birthday = LocalDate.of(year, month, day);
		
		Period years= Period.between(birthday, today);		

		if(years.getYears() >= 18){
			return true;
		}else{
				return false;
			}
		


	
	}
	public static boolean isValidPesel(String pesel){
		
		int[] peselNumbers= new int[11];
		
		if(pesel.length() !=11){
			return false;
		}else{
			for(int i=0; i< 11; i++){
				String temp=""+pesel.charAt(i);
				peselNumbers[i]= Integer.parseInt(temp);
			}
			
			
			int day=10*peselNumbers[4]+peselNumbers[5];
			int year=getYear(peselNumbers);
			int month=getMonth(peselNumbers);
			
			if(checkSum(peselNumbers) && checkMonth(month) && checkDay(day,month,year)){
				return true;
			}else{
				return false;
			}
			
		}		

	}

	private static int getMonth(int[] peselNumbers) {
		int month=10*peselNumbers[2]+peselNumbers[3];
		if (month > 80 && month < 93) {
			month -= 80;
			}
			else if (month > 20 && month < 33) {
			month -= 20;
			}
			else if (month > 40 && month < 53) {
			month -= 40;
			}
			else if (month > 60 && month < 73) {
			month -= 60;
			}          
			return month;

	}

	private static int getYear(int[] peselNumbers) {
		int year=10*peselNumbers[0]+peselNumbers[1];
		int month=10*peselNumbers[2]+peselNumbers[3];
		if (month > 80 && month < 93) {
			year += 1800;
			}
			else if (month > 0 && month < 13) {
			year += 1900;
			}
			else if (month > 20 && month < 33) {
			year += 2000;
			}
			else if (month > 40 && month < 53) {
			year += 2100;
			}
			else if (month > 60 && month < 73) {
			year += 2200;
			}          
			return year;
	}

	private static boolean checkDay(int day, int month, int year) {
		if ((day >0 && day < 32) &&
				(month == 1 || month == 3 || month == 5 ||
				month == 7 || month == 8 || month == 10 ||
				month == 12)) {
				return true;
				}
				else if ((day >0 && day < 31) &&
				(month == 4 || month == 6 || month == 9 ||
				month == 11)) {
				return true;
				} 
				else if ((day >0 && day < 30 && leapYear(year)) ||
				(day >0 && day < 29 && !leapYear(year))) {
				return true;
				}
				else {
				return false;
				}      

	}

	private static boolean leapYear(int year) {
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
			return true;
			else{
			return false;
			}
		}
	

	private static boolean checkSum(int[] peselNumbers) {
		int[] weights= {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
		int sum=0;
		for(int i=0; i<weights.length; i++){
			sum+= weights[i]*peselNumbers[i];
		}
		sum %= 10;
		sum = 10 - sum;
		sum %= 10;
		
		if(sum== peselNumbers[10]){
			return true;
		}else{
		return false;
		}
	}

	private static boolean checkMonth(int month) {

		if (month > 0 && month < 13) {
			return true;
			}
			else {
			return false;
			}
	}
	
	public static boolean isPeselBlackListed(String pesel){
		String urlString="http://webtask.future-processing.com:8069/blocked";
		String[] blackListedPesels= NetClient.getBlackListedPesels(NetClient.getDocument(urlString));
		
		
		
		for(String temp: blackListedPesels){
			if(temp.equals(pesel)){
				return true;
			}
		}
		return false;
		
		
	}


}
