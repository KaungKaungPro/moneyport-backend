package sg.nus.iss.adproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.adproject.entities.simulation.MktSimParam;
import sg.nus.iss.adproject.repositories.MktSimParamRepository;

@Service
@Transactional
public class VirtualTradeAdminService implements VirtualTradeAdminInterface {

	@Autowired
	private MktSimParamRepository mspr;
	
	@Override
	public void saveMktSimParam(MktSimParam param) {
		mspr.save(param);
	}
}
