package sg.nus.iss.adproject.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.adproject.entities.simulation.StockTrade;
import sg.nus.iss.adproject.entities.simulation.TradeBundle;

public interface StockTradeRepository extends JpaRepository<StockTrade, Integer>{

	@Query("SELECT st FROM StockTrade st WHERE st.stock.stockCode = :stockCode AND bundle=0 AND (st.tradeEnvOwner.id =:userId OR st.tradeEnvOwner.id = 3) AND st.dateTraded = :date ORDER BY st.timeTraded")
	List<StockTrade> getStockTradeByStockCodeByIntraDay(@Param("stockCode") String stockCode, @Param("date") LocalDate date, @Param("userId") long userId);
	
	@Query("SELECT st FROM StockTrade st WHERE st.stock.stockCode = :stockCode AND bundle = 3 AND (st.tradeEnvOwner.id =:userId OR st.tradeEnvOwner.id = 3) AND (st.dateTraded BETWEEN :startDate AND :endDate) ORDER BY st.dateTraded ASC")
	List<StockTrade> getStockTradeByStockCodeByDateRange(@Param("stockCode") String stockCode, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") long userId);
	
	@Query("SELECT st FROM StockTrade st WHERE st.stock.stockCode = :stockCode AND bundle = 3 AND (st.tradeEnvOwner.id =:userId OR st.tradeEnvOwner.id = 3)")
	List<StockTrade> getStockTradeByStockCodeMax(@Param("stockCode") String stockCode, @Param("userId") long userId);
	
	@Query("SELECT st FROM StockTrade st WHERE st.dateTraded = :dateTraded  AND (st.tradeEnvOwner.id =:userId OR st.tradeEnvOwner.id = 3) ORDER BY st.timeTraded ASC, st.stock.stockCode")
	List<StockTrade> findAllStockTradesByDateOrderByTimeAndStock(@Param("dateTraded") LocalDate dateTraded, @Param("userId") long userId);
	
	@Query("SELECT st FROM StockTrade st WHERE st.dateTraded = :dateTraded AND bundle =:bundle AND (st.tradeEnvOwner.id =:userId OR st.tradeEnvOwner.id = 3) ORDER BY st.timeTraded ASC, st.stock.stockCode")
	List<StockTrade> findAllBundledStockTradesByDateOrderByTimeAndStock(@Param("dateTraded") LocalDate dateTraded, @Param("bundle") TradeBundle bundle, @Param("userId") long userId);
	
	@Query("SELECT DISTINCT(st.dateTraded) FROM StockTrade st WHERE (st.tradeEnvOwner.id =:ownerId) AND st.dateTraded >= :gameStartDate ORDER BY st.dateTraded DESC")
	List<LocalDate> findLastTradeBuildDateByUser(@Param("ownerId") long ownerId, @Param("gameStartDate") LocalDate gameStartDate);
	
	@Query("SELECT DISTINCT(st.dateTraded) FROM StockTrade st WHERE st.tradeEnvOwner.id = 3 AND st.dateTraded > :originalRealWorldDataCutoffDate ORDER BY st.dateTraded DESC")
	List<LocalDate> findTradeDatesFromRealWorld(@Param("originalRealWorldDataCutoffDate") LocalDate originalRealWorldDataCutoffDate);
	
	@Modifying
	@Query("DELETE FROM StockTrade st WHERE (st.tradeEnvOwner.id =:ownerId OR st.tradeEnvOwner.id = 3) AND st.dateTraded < :cutoffDate and st.bundle = 0")
	void optimizePerMinuteTrade(@Param("ownerId") long ownerId, @Param("cutoffDate") LocalDate cutoffDate);
	
	@Query("SELECT st FROM StockTrade st WHERE (st.tradeEnvOwner.id =:ownerId OR st.tradeEnvOwner.id = 3) AND st.dateTraded =:trade_date AND st.bundle = 3")
	List<StockTrade> getBundledSingleDayTradeByDate(@Param("trade_date") LocalDate tradeDate, @Param("ownerId") long ownerId);

	@Query("SELECT st FROM StockTrade st WHERE st.tradeEnvOwner.id =:ownerId AND st.dateTraded =:trade_date AND st.bundle = 3")
	List<StockTrade> getBundledSingleDayTradeByDateByUserId(@Param("trade_date") LocalDate tradeDate, @Param("ownerId") long ownerId);

	@Modifying
	@Query("DELETE FROM StockTrade st WHERE st.tradeEnvOwner.id =:userId AND st.dateTraded >= :gameFirstTradeDate")
	void clearStockTradeByUserIdAndGameFirstTradeDate(@Param("userId") Long userId, @Param("gameFirstTradeDate") LocalDate gameFirstTradeDate);
}
