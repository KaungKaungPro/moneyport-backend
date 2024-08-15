package sg.nus.iss.adproject.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sg.nus.iss.adproject.entities.StockTracking;
import sg.nus.iss.adproject.repositories.StockTradeRepository;

public class TradeDate {
	
	public static List<DayOfWeek> nonTradeDayOfWeek = List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
	
	public static LocalDate realWorldTradeDataLastDate(StockTradeRepository str) {
		List<LocalDate> realWorldDataTradeDates = str.findTradeDatesFromRealWorld(StockTracking.originalRealWorldDataCutoffDate);
		realWorldDataTradeDates.forEach(System.out::println);
		if(!realWorldDataTradeDates.isEmpty()) {
			return realWorldDataTradeDates.get(0);
		} else {
			return StockTracking.originalRealWorldDataCutoffDate;
		}
	}
	
	public static LocalDate getLastTradeDate(List<LocalDate> tradeBuildDates, LocalDate dayBeforeGameStartDate) {
		if(!tradeBuildDates.isEmpty()) {
			return tradeBuildDates.get(0);
		} else {
			return dayBeforeGameStartDate;
		}
	}
	
	public static List<LocalDate> getTradeDates(LocalDate start, LocalDate end) {
		start = TradeDate.getNextTradeDateAfter(start);
		List<LocalDate> tradeDates = new ArrayList<LocalDate>();
		while(start.isBefore(end) || start.equals(end)) {
			if(!nonTradeDayOfWeek.contains(start.getDayOfWeek())) {
				tradeDates.add(start);
			}
			start = start.plusDays(1);
		}
		return tradeDates;
		
	}
	
	public static LocalDate getLatestTradeDateBefore(LocalDate date) {
		DayOfWeek dow = date.getDayOfWeek();
		if(dow.equals(DayOfWeek.SATURDAY)) {
			return date.minusDays(1);
		} else if (dow.equals(DayOfWeek.SUNDAY)) {
			return date.minusDays(2);
		} else if (dow.equals(DayOfWeek.MONDAY)){
			return date.minusDays(3);
		} else {
			return date.minusDays(1);
		}
	}
	
	public static LocalDate getTradeDateBeforePreviousTradeDate(LocalDate date) {
		DayOfWeek dow = date.getDayOfWeek();
		if (dow.equals(DayOfWeek.SUNDAY)) {
			return date.minusDays(3);
		} else if (dow.equals(DayOfWeek.MONDAY) || dow.equals(DayOfWeek.TUESDAY)){
			return date.minusDays(4);
		} else {
			return date.minusDays(2);
		}
	}
	
	public static LocalDate getNextTradeDateAfter(LocalDate date) {
		DayOfWeek dow = date.getDayOfWeek();
		if (dow.equals(DayOfWeek.FRIDAY)){
			return date.plusDays(3);
		} else if(dow.equals(DayOfWeek.SATURDAY)) {
			return date.plusDays(2);
		} else if (dow.equals(DayOfWeek.SUNDAY)) {
			return date.plusDays(1);
		} else {
			return date.plusDays(1);
		}
	}
	
	public static DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	public static int CountGameDays(LocalDate startDate, LocalDate gameDate) {
		int count = 0;
		LocalDate date = startDate;
		while(!date.isAfter(gameDate)) {
			if(!nonTradeDayOfWeek.contains(date.getDayOfWeek())){
				count++;
			}
			date = date.plusDays(1);
		}
		return count;
	}
}
