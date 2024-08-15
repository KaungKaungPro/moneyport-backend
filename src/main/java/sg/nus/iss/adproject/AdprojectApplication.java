package sg.nus.iss.adproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.scheduling.annotation.EnableAsync;
import sg.nus.iss.adproject.io.ContextLoader;
import sg.nus.iss.adproject.repositories.MktSimParamRepository;
import sg.nus.iss.adproject.repositories.PortfolioRepository;
import sg.nus.iss.adproject.repositories.PortfolioStockRepository;
import sg.nus.iss.adproject.repositories.StockRepository;
import sg.nus.iss.adproject.repositories.StockTradeRepository;
import sg.nus.iss.adproject.repositories.UserRepository;

@EnableAsync
@SpringBootApplication
public class AdprojectApplication {
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String ddlauto;
	
	private String basePath = "C:\\Users\\kaung\\OneDrive\\Documents\\Projects\\SpringProjects\\GDipSA-ADProject";

	public static void main(String[] args) {
		SpringApplication.run(AdprojectApplication.class, args);
	}

	
	@Bean
	CommandLineRunner loadContext(StockTradeRepository str, UserRepository ur, StockRepository sr, MktSimParamRepository mspr, PortfolioStockRepository psr, PortfolioRepository pr) {
		return args -> {
			// Load market simulation parameters if it is not loaded.
			ContextLoader cl = new ContextLoader(0, basePath);
			cl.LoadFirstParam(mspr);
			cl.LoadStocksAndTrades(str, sr, ur);
			// Load some portfolios
			// cl.BuildDefaultPortfolios(ur, sr, psr, pr);
		};
	}
	
//	@Bean
//	CommandLineRunner loadContext(StockTradeRepository str, StockRepository sr, UserRepository ur) {
//		return args -> {
//			if(ddlauto.equals("create")) {
//				ContextLoader cl = new ContextLoader(0, basePath);
//				cl.LoadStocksAndTrades(str, sr, ur);
//			}
//		};
//	}
}
