

public class Main {

	private static Model model;
	
	public static void main(String[] args) {
						
		System.out.println("Running...");		
		
		model = Model.getInstance();
		initializeModel(model);
		View view = new View(model);
		model.registerObserver(view); //connection Model -> View
		view.receiveUsersMessages();
	}
	
	public static void initializeModel(Model model){
	}
}