package sg.nus.iss.adproject.entities.simulation;

public enum TradeBundle {
	minute, minutes10, minutes30, day;
	
	public static TradeBundle getBundleBySize(int minute) {
		switch(minute) {
		case 1:
			return TradeBundle.minute;
		case 10:
			return TradeBundle.minutes10;
		case 30:
			return TradeBundle.minutes30;
		case 1440:
			return TradeBundle.day;
		default:
			return TradeBundle.minute;
		}
	}
}
