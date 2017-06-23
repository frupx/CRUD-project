package electoral.calculator;

import java.io.UnsupportedEncodingException;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;


public class Encryption {
	
	public static String encrypt(String input ){
		
		String hash="";
		
		 try {
			    SHA3.DigestSHA3 md=new SHA3.DigestSHA3(512);
			    md.update(input.getBytes("UTF-8"));
			    hash=Hex.toHexString(md.digest());
			   // System.out.println(hash);
			   
			  }
			 catch (  UnsupportedEncodingException e) {
			  //  logger.error("Error while hashing with SHA-3",e);
			  }
		 
		 return hash;
		
		
	}

}
