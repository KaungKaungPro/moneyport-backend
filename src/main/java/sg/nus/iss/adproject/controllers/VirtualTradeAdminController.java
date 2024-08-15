package sg.nus.iss.adproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sg.nus.iss.adproject.entities.simulation.MktSimParam;
import sg.nus.iss.adproject.services.VirtualTradeAdminInterface;

@RestController
@CrossOrigin
@RequestMapping("/api/vta")
public class VirtualTradeAdminController {

	@Autowired
	private VirtualTradeAdminInterface vtaService;
	
	@PostMapping("/saveMktSimParam")
	public ResponseEntity<String> saveMktSimParam(MktSimParam param){

		vtaService.saveMktSimParam(param);
		return new ResponseEntity<>("Parameters saved", HttpStatus.OK);
	}
	
}
