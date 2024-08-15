package sg.nus.iss.adproject.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import jakarta.servlet.http.HttpSession;
import sg.nus.iss.adproject.entities.Role;
import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.entities.simulation.Portfolio;
import sg.nus.iss.adproject.entities.simulation.VirtualTradingGame;
import sg.nus.iss.adproject.services.PortfolioInterface;
import sg.nus.iss.adproject.services.UserService;
import sg.nus.iss.adproject.services.VirtualTradeService;
import sg.nus.iss.adproject.utils.ParamUtils;
import sg.nus.iss.adproject.utils.TokenUtils;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class CommonController {
	
	@Autowired
	private UserService uService;
	
	@Autowired
	private PortfolioInterface pService;
	
	@Autowired
	private VirtualTradeService vtService;
	
	@GetMapping(value="/")
	public ResponseEntity<Long> home(HttpSession session){
		User defaultUser = uService.getDefaultUser();
		session.setAttribute("loggedInUser", defaultUser);
		System.out.println("Default user logged in: " + defaultUser.getFirstName());
		if(defaultUser != null) {
			defaultUser.setPassword(null);
			return new ResponseEntity<>(defaultUser.getId(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestParam Map<String, String> user){
		String userString = user.toString();
		String username = ParamUtils.extract("username", userString);
		String password = ParamUtils.extract("password", userString);
		User res = uService.login(username, password);
		if(res != null) {
			String token = TokenUtils.createToken(res.getId(), res.getPassword());
			Map<String, String> userMap = new HashMap<String, String>();
			userMap.put("username", res.getUsername());
			userMap.put("token", token);
			userMap.put("userId", res.getId().toString());

			userMap.put("role", res.getRole().toString());

			userMap.put("hasActiveGame", String.valueOf(res.getVtGame().getGameStartDate() != null));

			return new ResponseEntity<>(userMap, HttpStatus.OK);
		}
		System.out.println("Not found");
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	
	@PostMapping(value="/create", consumes="application/json")
	public ResponseEntity<Map> createAccount(@RequestBody User user){
		System.out.println(user);
		User u = uService.getUserByUsername(user.getUsername());
		Map<String, String> responseMap = new HashMap<>();
		if(u == null && User.validateUser(user)) {
			user.setRole(Role.Ordinary);
			VirtualTradingGame vtGame = new VirtualTradingGame();
			vtGame.setV$(100000.0);
			user.setVtGame(vtGame);
			uService.create(user);
			String token = TokenUtils.createToken(user.getId(), user.getPassword());
			Map<String, String> userMap = new HashMap<String, String>();
			userMap.put("username", user.getUsername());
			userMap.put("token", token);
			userMap.put("userId", user.getId().toString());
			userMap.put("hasActiveGame", String.valueOf(false));
			return new ResponseEntity<>(userMap, HttpStatus.OK);
		}
		responseMap.put("responseText", "Username already exists. ");
		
		return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/createInitialPortfolio/{userId}")
	public ResponseEntity<String> createInitialPortfolio(@PathVariable("userId") long userId){
		User user = uService.getUserByUserId(userId);
		Portfolio firstPortfolio = new Portfolio();
		firstPortfolio.setPortfolioName("My First Portfolio");
		firstPortfolio.setIndex(true);
		firstPortfolio.setUser(user);
		pService.create(firstPortfolio);
		System.out.println("Created portfolio");
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
