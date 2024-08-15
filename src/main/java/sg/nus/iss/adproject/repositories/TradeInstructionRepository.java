package sg.nus.iss.adproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.nus.iss.adproject.entities.simulation.TradeInstruction;


public interface TradeInstructionRepository extends JpaRepository<TradeInstruction, Integer> {
	
}
