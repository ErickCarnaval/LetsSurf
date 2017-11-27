
public class Wave {
	
	private String date;
	private String shaking;
	private String height;
	private String direction;
	private String wind;
	private String windDirection;
		
	public Wave(String date, String shaking, String height, String direction, String wind, String windDirection) {
		super();
		this.date = date;
		this.shaking = shaking;
		this.height = height;
		this.direction = direction;
		this.wind = wind;
		this.windDirection = windDirection;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getShaking() {
		return shaking;
	}

	public void setShaking(String shaking) {
		this.shaking = shaking;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}
	
}
