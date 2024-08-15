package sg.nus.iss.adproject;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import sg.nus.iss.adproject.entities.simulation.StockTrade;
import sg.nus.iss.adproject.repositories.StockRepository;
import sg.nus.iss.adproject.repositories.StockTradeRepository;
import sg.nus.iss.adproject.utils.TradeDate;

@SpringBootTest
class AdprojectApplicationTests {

	
	@Autowired
	private StockTradeRepository str;
	
	@Autowired
	private StockRepository sr;
	
	
	
	@Test
	void contextLoads() {
		List<LocalDate> tradeBuildDates = str.findLastTradeBuildDateByUser(28L, LocalDate.of(2024, 8, 7));
		tradeBuildDates.forEach(System.out::println);
		
		
		List<LocalDate> testDates = TradeDate.getTradeDates(LocalDate.of(2024, 8, 7), TradeDate.getLatestTradeDateBefore(LocalDate.of(2024, 8, 8)));
		assertThat(testDates.size()).isEqualTo(1);
		assertThat(testDates.get(0)).isEqualTo(LocalDate.of(2024, 8, 7));
	}
	
	@Test
	void testLastTradeDate() {
		LocalDate lastRealWorldDataTradeDate = TradeDate.realWorldTradeDataLastDate(str);
		assertThat(lastRealWorldDataTradeDate).isEqualTo(LocalDate.of(2024, 7, 26));
		
		
	}

}
