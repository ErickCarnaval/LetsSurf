import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;


public class Connection {
	private String urlPrefix = "http://servicos.cptec.inpe.br/XML/listaCidades?city=";
	private String urlId = "";
	
	public List<City> cityList(String url){
		URL urlConection;
        HttpURLConnection urlConnection = null;
        List<City> xmlList = null;
        
        try {
            urlConection = new URL(urlPrefix + url);
            urlConnection = (HttpURLConnection) urlConection.openConnection();
            InputStream in = urlConnection.getInputStream();
            xmlList = parse(in);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace(); //If you want further info on failure...
            }
        }	
		return xmlList;				
	}
	
// ####################################################################################
	
	private List<City> parse(InputStream in){		
		List<City> found = new ArrayList<City>();
		String nome = null;
		String uf = null;
		String id = null;
		
		try{			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(in);
			doc.getDocumentElement().normalize();			
			NodeList nList = doc.getElementsByTagName("cidade");			
			
			for(int temp = 0; temp < nList.getLength(); temp++){
				Node nNode = nList.item(temp);				
				
				if(nNode.getNodeType() == Node.ELEMENT_NODE){
					Element eElement = (Element) nNode;					
					NodeList cityNameList = eElement.getChildNodes();					
					
					for(int count = 0; count < cityNameList.getLength(); count++){
						Node node1 = cityNameList.item(count);
						
						if(cityNameList.item(count).getNodeName().equals("nome")){
							Element city = (Element) node1;
							nome = city.getTextContent();							
						}else if(cityNameList.item(count).getNodeName().equals("uf")){
							Element city = (Element) node1;
							uf = city.getTextContent();
						}else if(cityNameList.item(count).getNodeName().equals("id")){
							Element city = (Element) node1;
							id = city.getTextContent();
						}												
					}
				}found.add(new City(nome, uf, id));
			}
		}catch(Exception e){
			e.printStackTrace();
		}return found;		
	}	
}
