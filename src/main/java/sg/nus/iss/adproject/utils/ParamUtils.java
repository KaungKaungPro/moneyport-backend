package sg.nus.iss.adproject.utils;

public class ParamUtils {
	// {instr[key]=0, instr[op]=1, instr[tradeEnvOwner]=1, instr[quantity]=100, instr[price]=54.95166274613885, instr[stockCode]=DIS, instr[stockName]=Disney}
	public static String extract(String target, String from) {
		// Strip first and last
		from = from.substring(1, from.length() - 1);

		String[] partsArray = from.split(", ");
		for(String part : partsArray) {
			if(part.contains(target)) {
				String[] valPart = part.split("=");
				return valPart[1];
			}
		}
		return null;
	}
	
}
