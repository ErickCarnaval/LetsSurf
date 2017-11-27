import java.util.LinkedList;
import java.util.List;

import com.pengrad.telegrambot.model.Update;

public class Model implements Subject{

	private List<Observer> observers = new LinkedList<Observer>();
	private List<City> cities = new LinkedList<City>();
	private List<Wave> predictions = new LinkedList<Wave>();
	private static Model uniqueInstance;
	private Model(){}

	public static Model getInstance(){
		if(uniqueInstance == null){
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void registerObserver(Observer observer){
		observers.add(observer);
	}

	public void notifyObservers(long chatId, String citiesData){
		for(Observer observer:observers){
			observer.update(chatId, citiesData);
		}
	}	
	//  = model.searchCity(update);
	public void addCity(City city){
		this.cities.add(city);
	}

	public void searchCity(Update update){
		cities.removeAll(cities);
		cities.addAll(new Connection().cityList("http://servicos.cptec.inpe.br/XML/listaCidades?city=" + update.message().text().replace(" ", "%20")));
		String citiesData = null;

		for(City city: cities){
			try{

				if(new Connection().predictionList("http://servicos.cptec.inpe.br/XML/cidade/" + city.getCityId() + "/dia/0/ondas.xml").isEmpty()){
					System.out.println("entrou no if new connection!");
					cities.remove(city);
				}
			}catch(Exception e){
				e.getMessage();
			}
		}

		if(!cities.isEmpty()){

			for(City city: cities){
				citiesData = city.getCityName() + " - " + city.getCityUf() + "\nid: " + city.getCityId();
				this.notifyObservers(update.message().chat().id(), citiesData);
			}
			this.notifyObservers(update.message().chat().id(), "Type 'N' to new search or 'id' to view id predictions!");
		}else{
			this.notifyObservers(update.message().chat().id(), "Sorry, city not found!");
		}

	}

	public void filterPrediction(Update update){

		System.out.println("entrou no metodo filterPrediction");

		predictions.removeAll(predictions);
		predictions.addAll(new Connection().predictionList("http://servicos.cptec.inpe.br/XML/cidade/" + update.message().text() + "/dia/0/ondas.xml"));
		String waveData = null;
		String horario = "12h Z";
		String periodo = "- manhã";

		if(predictions.isEmpty()) {
			System.out.println("Lista vazia");
			this.notifyObservers(update.message().chat().id(), "Esta não é uma cidade litorânea!");
			this.notifyObservers(update.message().chat().id(), "Digite 'city' para lista de 'IDs' ou 'id'!");
		}else{

			for(Wave wave: predictions){

				waveData = "data: " + wave.getDate().replace(horario, periodo) + "\naltura da onda: " + wave.getHeight() + "\ndireção da onda: " + wave.getDirection() +
						"\nagitação da onda: " + wave.getShaking() + "\nvento: " + wave.getWind() + "\ndireção do vento: " + wave.getWindDirection();

				this.notifyObservers(update.message().chat().id(), waveData);

				if(periodo.equals("- tarde")){
					horario = "21h Z";
					periodo = "- noite";
				}else{
					horario = "18h Z";
					periodo = "- tarde";
				}
			}
			this.notifyObservers(update.message().chat().id(), "Type 'N' to new search or 'id' to view id predictions!");
		}	
	}

}
