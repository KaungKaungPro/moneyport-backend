package sg.nus.iss.adproject.io;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sg.nus.iss.adproject.entities.AssetClass;
import sg.nus.iss.adproject.entities.Role;
import sg.nus.iss.adproject.entities.StockTracking;
import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.entities.simulation.MktSimParam;
import sg.nus.iss.adproject.entities.simulation.Portfolio;
import sg.nus.iss.adproject.entities.simulation.PortfolioStock;
import sg.nus.iss.adproject.entities.simulation.Stock;
import sg.nus.iss.adproject.entities.simulation.StockTrade;
import sg.nus.iss.adproject.entities.simulation.TradeBundle;
import sg.nus.iss.adproject.repositories.MktSimParamRepository;
import sg.nus.iss.adproject.repositories.PortfolioRepository;
import sg.nus.iss.adproject.repositories.PortfolioStockRepository;
import sg.nus.iss.adproject.repositories.StockRepository;
import sg.nus.iss.adproject.repositories.StockTradeRepository;
import sg.nus.iss.adproject.repositories.UserRepository;
import sg.nus.iss.adproject.utils.ValueRound;

public class ContextLoader {
	
	private String path;
	
	private RandDataGenerator randGen;
	
	public ContextLoader(int seed, String path) {
		super();
		this.path = path;
		this.randGen = new RandDataGenerator(seed);
	}
	
	public void CreateData(UserRepository ur, StockRepository sr) {
		BuildUsers(ur);
		BuildStocks(sr);
	}
	
	public void ReloadATT(StockRepository sr) {
		LoadStocks(sr);
	}
	
	private BufferedReader PrepareToRead(String csv) {
		try {
			FileReader fr = new FileReader(path + "\\" + csv);
			return new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	public void LoadStocksAndTrades(StockTradeRepository str, StockRepository sr, UserRepository ur) {
//		BuildUsers(ur);
//		LoadStocks(sr);
		LoadStockTrades(str, sr);
	}
	
	private void LoadStocks(StockRepository sr) {
//		Stock s1 = new Stock("JNJ", "Johnson & Johnson", "USD", 160.64, 160.64);
//		s1.setIPOyear(2004);
//		s1.setaClass(AssetClass.A1);
//		sr.save(s1);
//		
//		Stock s2 = new Stock("PG", "Procter & Gamble", "USD", 155.28, 155.28);
//		s2.setIPOyear(2008);
//		s2.setaClass(AssetClass.A1);
//		sr.save(s2);
//		
//		Stock s3 = new Stock("KOF", "Coca Cola", "USD", 87.75, 87.75);
//		s3.setIPOyear(2004);
//		s3.setaClass(AssetClass.A1);
//		sr.save(s3);
//		
//		Stock s4 = new Stock("VZ", "Verizon", "USD", 36.95, 36.95);
//		s4.setIPOyear(2017);
//		s4.setaClass(AssetClass.A1);
//		sr.save(s4);
//		
//		Stock s5 = new Stock("WMT", "Walmart", "USD", 69.78, 69.78);
//		s5.setIPOyear(2004);
//		s5.setaClass(AssetClass.A1);
//		sr.save(s5);
//		
//		Stock s6 = new Stock("MCD", "MacDonald", "USD", 252, 252);
//		s6.setIPOyear(2004);
//		s6.setaClass(AssetClass.A1);
//		sr.save(s6);
//		
//		Stock s7 = new Stock("PEP", "PepsiCo", "USD", 172.75, 172.75);
//		s7.setIPOyear(2004);
//		s7.setaClass(AssetClass.A1);
//		sr.save(s7);
//		
		Stock s8 = new Stock("T", "AT&T", "USD", 19.33);
		s8.setIPOyear(2022);
		s8.setaClass(AssetClass.A1);
		sr.save(s8);
//		
//		Stock s9 = new Stock("UTG.MU", "Unilever", "EUR", 0.122, 0.122);
//		s9.setIPOyear(2004);
//		s9.setaClass(AssetClass.A1);
//		sr.save(s9);
//		
//		Stock s10 = new Stock("XOM", "ExxonMobile", "USD", 117.33, 117.33);
//		s10.setIPOyear(2004);
//		s10.setaClass(AssetClass.A1);
//		sr.save(s10);
//		
//		Stock s11 = new Stock("AAPL", "Apple Inc", "USD", 217.96, 217.96);
//		s11.setIPOyear(2004);
//		s11.setaClass(AssetClass.A2);
//		sr.save(s11);
//		
//		Stock s12 = new Stock("MSFT", "Microsoft", "USD", 425.27, 425.27);
//		s12.setIPOyear(2004);
//		s12.setaClass(AssetClass.A2);
//		sr.save(s12);
//		
//		Stock s13 = new Stock("GOOGL", "Alphabet Inc", "USD", 167, 167);
//		s13.setIPOyear(2004);
//		s13.setaClass(AssetClass.A2);
//		sr.save(s13);
//		
//		Stock s14 = new Stock("AMZN", "Amazon", "USD", 182.5, 182.5);
//		s14.setIPOyear(2004);
//		s14.setaClass(AssetClass.A2);
//		sr.save(s14);
//		
//		Stock s15 = new Stock("META", "Meta Platforms, Inc", "USD", 465.7, 465.7);
//		s15.setIPOyear(2012);
//		s15.setaClass(AssetClass.A2);
//		sr.save(s15);
//		
//		Stock s16 = new Stock("V", "Visa", "USD", 259.46, 259.46);
//		s16.setIPOyear(2008);
//		s16.setaClass(AssetClass.A2);
//		sr.save(s16);
//		
//		Stock s17 = new Stock("JPM", "JPMorgan Chase", "USD", 212.24, 212.24);
//		s17.setIPOyear(2004);
//		s17.setaClass(AssetClass.A2);
//		sr.save(s17);
//		
//		Stock s18 = new Stock("DIS", "Disney", "USD", 89.93, 89.93);
//		s18.setIPOyear(2004);
//		s18.setaClass(AssetClass.A2);
//		sr.save(s18);
//		
//		Stock s19 = new Stock("NFLX", "Netflix", "USD", 631.37, 631.37);
//		s19.setIPOyear(2004);
//		s19.setaClass(AssetClass.A2);
//		sr.save(s19);
//		
//		Stock s20 = new Stock("CSCO", "Cisco Systems", "USD", 47.88, 47.88);
//		s20.setIPOyear(2004);
//		s20.setaClass(AssetClass.A2);
//		sr.save(s20);
//		
//		Stock s21 = new Stock("TSLA", "Tesla", "USD", 219.8, 219.8);
//		s21.setIPOyear(2010);
//		s21.setaClass(AssetClass.A3);
//		sr.save(s21);
//		
//		Stock s22 = new Stock("NVDA", "NVIDIA", "USD", 113.06, 113.06);
//		s22.setIPOyear(2004);
//		s22.setaClass(AssetClass.A3);
//		sr.save(s22);
//		
//		Stock s23 = new Stock("AMD", "Advanced Micro Devices", "USD", 139.99, 139.99);
//		s23.setIPOyear(2004);
//		s23.setaClass(AssetClass.A3);
//		sr.save(s23);
//		
//		Stock s24 = new Stock("COIN", "Coinbase", "USD", 242.93, 242.93);
//		s24.setIPOyear(2021);
//		s24.setaClass(AssetClass.A3);
//		sr.save(s24);
//		
//		Stock s25 = new Stock("SQ", "Block, Inc", "USD", 60.18, 60.18);
//		s25.setIPOyear(2015);
//		s25.setaClass(AssetClass.A3);
//		sr.save(s25);
//		
//		Stock s26 = new Stock("PLTR", "Palantir Technology", "USD", 27.18, 27.18);
//		s26.setIPOyear(2020);
//		s26.setaClass(AssetClass.A3);
//		sr.save(s26);
//		
//		Stock s27 = new Stock("NIO", "Nio Inc", "USD", 4.43, 4.43);
//		s27.setIPOyear(2018);
//		s27.setaClass(AssetClass.A3);
//		sr.save(s27);
//		
//		Stock s28 = new Stock("BYND", "Beyond Meat", "USD", 6.25, 6.25);
//		s28.setIPOyear(2019);
//		s28.setaClass(AssetClass.A3);
//		sr.save(s28);
//		
//		Stock s29 = new Stock("PLUG", "Plug Power", "USD", 2.49, 2.49);
//		s29.setIPOyear(2004);
//		s29.setaClass(AssetClass.A3);
//		sr.save(s29);
//		
//		Stock s30 = new Stock("SPCE", "Virgin Galactic", "USD", 7.45, 7.45);
//		s30.setIPOyear(2017);
//		s30.setaClass(AssetClass.A3);
//		sr.save(s30);
	}
	
	private void LoadStockTrades(StockTradeRepository str, StockRepository sr) {
		
		try {
//			for(String sc : StockTracking.Stocks) {
				BufferedReader br = PrepareToRead("T.csv");
				String x;
				br.readLine();
				Stock s = sr.findByStockCode("T");
				while((x = br.readLine()) != null) {
					List<String> dat = List.of(x.split(","));
					StockTrade st = new StockTrade();
					DateTimeFormatter dashFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					DateTimeFormatter slashFormat = DateTimeFormatter.ofPattern("d/M/yyyy");
					st.setStock(s);
					st.setTradeEnvOwner(User.adminUser());
					st.setDateTraded(LocalDate.parse(dat.get(0), dat.get(0).contains("-") ? dashFormat : dat.get(0).contains("/") ? slashFormat : DateTimeFormatter.ISO_DATE));
					st.setOpen(BigDecimal.valueOf(ValueRound.RoundTo(Double.parseDouble(dat.get(1)), 2)));
					st.setHigh(BigDecimal.valueOf(ValueRound.RoundTo(Double.parseDouble(dat.get(2)), 2)));
					st.setLow(BigDecimal.valueOf(ValueRound.RoundTo(Double.parseDouble(dat.get(3)), 2)));
					st.setClose(BigDecimal.valueOf(ValueRound.RoundTo(Double.parseDouble(dat.get(4)), 2)));
					st.setVolume(Long.parseLong(dat.get(6)));
					st.setBundle(TradeBundle.day);
					str.save(st);
				}
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	private void BuildUsers(UserRepository urepo) {
		User user = new User();
		user.setFirstName(randGen.genFirstName());
		user.setLastName(randGen.genLastName());
		user.setUsername(randGen.getUsernameFromFirstAndLastName(user.getFirstName(), user.getLastName()));
		user.setEmail(randGen.getEmailFromUsername(user.getUsername()));
		user.setPassword("password");
		user.setRole(Role.Ordinary);
		urepo.save(user);
		
		User admin = new User();
		admin.setFirstName(randGen.genFirstName());
		admin.setLastName(randGen.genLastName());
		admin.setUsername(randGen.getUsernameFromFirstAndLastName(admin.getFirstName(), admin.getLastName()));
		admin.setEmail(randGen.getEmailFromUsername(admin.getUsername()));
		admin.setPassword("password");
		admin.setRole(Role.Admin);
		urepo.save(admin);
		
		User admin2 = new User();
		admin2.setFirstName("admin");
		admin2.setLastName("admin");
		admin2.setUsername("admin");
		admin2.setEmail("admin@mymail.com");
		admin2.setPassword("password");
		admin2.setRole(Role.Admin);
		urepo.save(admin2);
	}
	
	private void BuildStocks(StockRepository srepo) {
		// Build A1 stocks
		AssetClass a1 = AssetClass.A1;
		for(int i = 0; i < 10; i++) {
			Stock stock = new Stock();
			stock.setStockCode(randGen.genStockCode());
			stock.setStockName(randGen.genStockName());
			stock.setCurrency(randGen.genCurrency());
			stock.setaClass(a1);
			stock.setOpenPrice(randGen.genPrice(a1));
//			stock.setLastTradePrice(stock.getOpenPrice());
			stock.setIPOyear(randGen.genIPOYear());
			srepo.save(stock);
		}
		
		// Build A2 stocks
		AssetClass a2 = AssetClass.A2;
		for(int i = 0; i < 10; i++) {
			Stock stock = new Stock();
			stock.setStockCode(randGen.genStockCode());
			stock.setStockName(randGen.genStockName());
			stock.setCurrency(randGen.genCurrency());
			stock.setaClass(a2);
			stock.setOpenPrice(randGen.genPrice(a2));
//			stock.setLastTradePrice(stock.getOpenPrice());
			stock.setIPOyear(randGen.genIPOYear());
			srepo.save(stock);
		}
		
		// Build A3 stocks
		AssetClass a3 = AssetClass.A3;
		for(int i = 0; i < 10; i++) {
			Stock stock = new Stock();
			stock.setStockCode(randGen.genStockCode());
			stock.setStockName(randGen.genStockName());
			stock.setCurrency(randGen.genCurrency());
			stock.setaClass(a3);
			stock.setOpenPrice(randGen.genPrice(a3));
//			stock.setLastTradePrice(stock.getOpenPrice());
			stock.setIPOyear(randGen.genIPOYear());
			srepo.save(stock);
		}
	}
	
	public void BuildDefaultPortfolios(UserRepository ur, StockRepository sr, PortfolioStockRepository psr, PortfolioRepository pr) {
		Portfolio pf = pr.findPortfolioById(1);
		
		List<PortfolioStock> pfStocks = new ArrayList<>();
		Stock jnj = sr.findByStockCode("JNJ");
		PortfolioStock jnjStock = new PortfolioStock();
		jnjStock.setId(1);
		jnjStock.setPurchasedPrice(159.33f);
		jnjStock.setQuantity(2000);
		jnjStock.setLastDateTraded(LocalDate.of(2024, 7, 25));
		jnjStock.setPortfolio(pf);
		jnjStock.setStock(jnj);
		psr.save(jnjStock);
		pfStocks.add(jnjStock);
		
		Stock kof = sr.findByStockCode("KOF");
		PortfolioStock kofStock = new PortfolioStock();
		kofStock.setId(2);
		kofStock.setPurchasedPrice(87.75f);
		kofStock.setQuantity(3000);
		kofStock.setLastDateTraded(LocalDate.of(2024, 7, 26));
		kofStock.setPortfolio(pf);
		kofStock.setStock(kof);
		psr.save(kofStock);
		pfStocks.add(kofStock);
		
		pf.setPortfolioStocks(pfStocks);
		pr.save(pf);
	}
	
	public void LoadFirstParam(MktSimParamRepository mspr) {
		if(mspr.findAll().isEmpty()) {
			mspr.save(MktSimParam.initialMktSimParam());
		}
	}

}
