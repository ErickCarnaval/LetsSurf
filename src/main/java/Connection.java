import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;


public class Connection {

	public List<City> cityList(String url){
		URL urlCon;
		HttpURLConnection urlConnection = null;
		List<City> xmlList = null;

		try {
			urlCon = new URL(url);
			urlConnection = (HttpURLConnection) urlCon.openConnection();
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
	// ########## generate list of waves conditions ##########
	public List<Wave> predictionList(String url){
		URL urlConection;
		HttpURLConnection urlConnection = null;
		List<Wave> xmlPred = new ArrayList<Wave>();

		try {
			urlConection = new URL(url);
			urlConnection = (HttpURLConnection) urlConection.openConnection();
			InputStream in = urlConnection.getInputStream();
			xmlPred = parsePred(in);

		} catch (IOException e) {
			//e.printStackTrace();
			return xmlPred;
		} finally {
			try {
				urlConnection.disconnect();
			} catch (Exception e) {
				e.printStackTrace(); //If you want further info on failure...
			}
		}	
		return xmlPred;	
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

	// ####################################################################################

	private List<Wave> parsePred(InputStream in){		
		List<Wave> found = new ArrayList<Wave>();
		String dia = null;
		String agitacao = null;
		String altura = null;
		String direcao = null;
		String vento = null;
		String vento_dir = null;

		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(in);
			doc.getDocumentElement().normalize();
			NodeList nList1;

			for(int i = 0; i < 3; i++){
				if(i == 0){
					nList1 = doc.getElementsByTagName("manha");
				}else if(i == 1){
					nList1 = doc.getElementsByTagName("tarde");
				}else{
					nList1 = doc.getElementsByTagName("noite");
				}

				for(int temp = 0; temp < nList1.getLength(); temp++){
					Node nNode = nList1.item(temp);

					if(nNode.getNodeType() == Node.ELEMENT_NODE){
						Element eElement = (Element) nNode;					
						NodeList periodList = eElement.getChildNodes();

						for(int count = 0; count < periodList.getLength(); count++){
							Node node1 = periodList.item(count);

							if(periodList.item(count).getNodeName().equals("dia")){
								Element morning = (Element) node1;
								dia = morning.getTextContent();
							}else if(periodList.item(count).getNodeName().equals("agitacao")){
								Element morning = (Element) node1;
								agitacao = morning.getTextContent();
							}else if(periodList.item(count).getNodeName().equals("altura")){
								Element morning = (Element) node1;
								altura = morning.getTextContent();
							}else if(periodList.item(count).getNodeName().equals("direcao")){
								Element morning = (Element) node1;
								direcao = morning.getTextContent();
							}else if(periodList.item(count).getNodeName().equals("vento")){
								Element morning = (Element) node1;
								vento = morning.getTextContent();
							}else if(periodList.item(count).getNodeName().equals("vento_dir")){
								Element morning = (Element) node1;
								vento_dir = morning.getTextContent();
							}							
						}found.add(new Wave(dia, agitacao, altura, direcao, vento, vento_dir));
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}return found;
	}		
}