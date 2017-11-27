import com.pengrad.telegrambot.model.Update;

public class ControllerFilterPrediction implements ControllerFilter{
	
	private Model model;
	private View view;
	
	public ControllerFilterPrediction(Model model, View view){
		this.model = model;
		this.view = view;
	}
	
	public void filter(Update update){		
		view.sendTypingMessage(update);
		model.filterPrediction(update);		
	}
}
