package sg.nus.iss.adproject.entities.simulation;

public enum TradeOp {
	Buy, Sell, BuyInstruction, SellInstruction;
	
	public static TradeOp ofValue(int val) {
		switch(val) {
		case 1:
			return TradeOp.Buy;
		case 2:
			return TradeOp.Sell;
		case 3:
			return TradeOp.BuyInstruction;
		case 4:
			return TradeOp.SellInstruction;
		default:
			return null;
		}
	}
}
