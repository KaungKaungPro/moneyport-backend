package sg.nus.iss.adproject.services;

import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.entities.simulation.TradeInstruction;

public interface TradeInstructionInterface {

	void saveTradeInstruction(TradeInstruction ti);
	
	void executeTradeInstruction(TradeInstruction ti, User user);
	
}
