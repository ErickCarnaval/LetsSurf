import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class View implements Observer{

	private TelegramBot bot = TelegramBotAdapter.build("436255217:AAE88_inc3SO13x5Ald1SjoM9LzGjpvHt80");
	//Object that receives messages
	GetUpdatesResponse updatesResponse;
	//Object that send responses
	SendResponse sendResponse;
	//Object that manage chat actions like "typing action"
	BaseResponse baseResponse;	
	int queuesIndex=0;	
	ControllerSearch controllerSearch; //Strategy Pattern -- connection View -> Controller
	ControllerFilter controllerFilter;
	boolean searchBehaviour = false;	
	private Model model;

	public View(Model model){
		this.model = model; 
	}

	public void setControllerSearch(ControllerSearch controllerSearch){ //Strategy Pattern
		this.controllerSearch = controllerSearch;
	}

	public void setControllerFilter(ControllerFilter controllerFilter){
		this.controllerFilter = controllerFilter;
	}

	public void receiveUsersMessages() {	

		//infinity loop
		while (true){

			//taking the Queue of Messages
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(queuesIndex));

			//Queue of messages
			List<Update> updates = updatesResponse.updates();

			//taking each message in the Queue
			for (Update update : updates) {

				//updating queue's index
				queuesIndex = update.updateId()+1;
				try{
					if(this.searchBehaviour==true){						

						if(Character.isLetter(updatesResponse.updates().get(0).message().text().charAt(0))){							
							this.callControllerSearch(update);
						}else{
							this.callControllerFilter(update);
						}

					} else if(update.message().text().toLowerCase().equals("city")){						
						setControllerSearch(new ControllerSearchCity(model, this));
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Type city name or part of it!"));
						this.searchBehaviour = true;
					}else if(update.message().text().toLowerCase().equals("id")){
						setControllerFilter(new ControllerFilterPrediction(model, this));
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Please, type city id!"));
						this.searchBehaviour = true;
					}else {
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Welcome to SufingBot!"));
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Type 'city' to find cities or type 'ID' to find prediction!"));
					}
				}catch(NullPointerException e){

				}
			}
		}		
	}

	public void callControllerSearch(Update update){
		this.controllerSearch.search(update);
	}

	public void callControllerFilter(Update update){
		this.controllerFilter.filter(update);
	}

	public void update(long chatId, String cityData){
		sendResponse = bot.execute(new SendMessage(chatId, cityData));
		this.searchBehaviour = false;
	}

	public void sendTypingMessage(Update update){
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

}