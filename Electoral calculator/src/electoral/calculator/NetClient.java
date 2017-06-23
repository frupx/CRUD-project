package electoral.calculator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NetClient {
	
	public static Document getDocument(String urlString){

		
		try{
		URL url= new URL(urlString);		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/xml");
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(conn.getInputStream());
		return doc;
		}catch(IOException e){
		
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		} catch (SAXException e) {

			e.printStackTrace();
		}
		return null;

	}
	
	public static String[] getXMLCandidates(Document doc){
		NodeList personList = doc.getElementsByTagName("candidate");
		String[] xmlLines= new String[personList.getLength()*2];

		int xmlLines_id=0;
		for (int i=0; i<personList.getLength(); i++){
			Node p = personList.item(i);

			
		if(p.getNodeType()==Node.ELEMENT_NODE){
				Element person = (Element) p;
				

				NodeList nameList = person.getChildNodes();
				

				for(int j=0; j<nameList.getLength(); j++){
					Node n = nameList.item(j);
					
					if(n.getNodeType()==Node.ELEMENT_NODE){
						Element name= (Element) n;
						xmlLines[xmlLines_id]=name.getTextContent();
						xmlLines_id++;

					}
				}
				
		}	//END OF IF STATEMENT
		} //END OF FOR LOOP
		
		return xmlLines;
	}
	
	
	
	public static String[] getBlackListedPesels(Document doc){
		NodeList personList = doc.getElementsByTagName("person");
		String[] xmlLines= new String[personList.getLength()];

		int xmlLines_id=0;
		for (int i=0; i<personList.getLength(); i++){
			Node p = personList.item(i);

			
		if(p.getNodeType()==Node.ELEMENT_NODE){
				Element person = (Element) p;
				

				NodeList nameList = person.getChildNodes();
				

				for(int j=0; j<nameList.getLength(); j++){
					Node n = nameList.item(j);
					
					if(n.getNodeType()==Node.ELEMENT_NODE){
						Element name= (Element) n;
						xmlLines[xmlLines_id]=name.getTextContent();
						xmlLines_id++;
					}
				}
				
		}	//END OF IF STATEMENT
		} //END OF FOR LOOP
		
		return xmlLines;
	}
	

}
