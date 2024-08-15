package sg.nus.iss.adproject.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ValueRound {
	
	public static double RoundTo(double val, int place) {
		return new BigDecimal(Double.toString(val))
				.setScale(2, RoundingMode.HALF_UP)
				.doubleValue();
	}

}
