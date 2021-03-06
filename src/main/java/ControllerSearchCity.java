import com.pengrad.telegrambot.model.Update;

public class ControllerSearchCity implements ControllerSearch{
	
	private Model model;
	private View view;
	
	public ControllerSearchCity(Model model, View view) {
		this.model = model;
		this.view = view;
	}
	
	public void search(Update update){
		
		view.sendTypingMessage(update);
		model.searchCity(update);
	}
}
