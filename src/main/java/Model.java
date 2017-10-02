import java.util.LinkedList;
import java.util.List;

import com.pengrad.telegrambot.model.Update;

public class Model implements Subject{
	
	private List<Observer> observers = new LinkedList<Observer>();
	private List<City> cities = new LinkedList<City>();	
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
	
	public void notifyObservers(long chatId, String studentsData){
		for(Observer observer:observers){
			observer.update(chatId, studentsData);
		}
	}
	
	public List searchCity(Update update){
		this.cities.addAll(new Connection().cityList(update.message().text()));
		String citiesData = null;
		for(City city: cities){
			citiesData = city.getCityName() + " - " + city.getCityUf() + "\nid: " + city.getCityId();
		}
		return cities;
	}

}
