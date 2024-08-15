package sg.nus.iss.adproject.io;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import sg.nus.iss.adproject.entities.AssetClass;
import sg.nus.iss.adproject.utils.ValueRound;

public class RandDataGenerator {
	
	private Random rand;
	
	public RandDataGenerator(int seed) {
		this.rand = new Random(seed);
	}
	
	
	public String genFirstName(){
		return firstNames[rand.nextInt(firstNames.length)];
	}
	
	public String genLastName() {
		return lastNames[rand.nextInt(lastNames.length)];
	}
	
	public String getUsernameFromFirstAndLastName(String firstName, String lastName) {
		return firstName.substring(0, Math.min(firstName.length(), 4)) + lastName.substring(0, Math.min(lastName.length(), 4));
	}
	
	public String getEmailFromUsername(String username) {
		return username + "@mymail.com";
	}
	
	public String[] firstNames = {"Kerri", "Brandon", "Robert", "Glenn", "Richard", "Michael", "Diana", "Danielle", "George", "Jaime", "Jeffery", "Amber", "Siva", "Rebecca", "Connie", "Jacob", "Angela", "Stephen", "Joanne", "Jamie", "Warren", "Nathaniel", "Patricia", "Patrick", "Annette", "Joseph", "Lee", "Jeffrey", "Justin", "Michelle", "David", "Anthony", "Kevin", "Janet", "Samuel", "Cindy", "Eric", "Zachary", "Bobby", "Brittany", "Amanda", "John", "James", "Katelyn", "Jason", "Renee", "Bonnie", "Stephanie", "Donald", "Frank", "Perry", "Helen", "Emma", "Brian", "Marc", "Jessica", "Heather", "Susan", "Shelby", "Tonya", "Jennifer", "Tina", "Christopher", "Deborah", "Benjamin", "Veronica", "Christine", "Jonathan", "Donna", "Colleen", "William", "Corey", "Kimberly", "Nancy", "Kelly", "Adam", "Andrea", "Chelsea", "Cory", "Aaron", "Amy"};
	public String[] lastNames = {"Garcia", "Myers", "Wolfe", "Sanders", "Lopez", "Wall", "Brown", "Reynolds", "Bolton", "Chen", "Campbell", "Friedman", "Mapo", "Newton", "Wise", "Soto", "Fritz", "Gilbert", "Miller", "Dunn", "Watson", "Anderson", "Edwards", "Parker", "Mitchell", "Collier", "Sexton", "Francis", "Cook", "Lam", "Lee", "Beard", "Gutierrez", "Johnson", "Kelley", "Stewart", "Graham", "Kelly", "Klein", "Wells", "Phillips", "Vega", "Wade", "Fuller", "James", "Lambert", "Bray", "Foster", "Short", "Riley", "Hudson", "Page", "Griffin", "Jones", "Owen", "Espinoza", "Carson", "Harvey", "Bass", "Clark", "Perkins", "Richard", "Wright", "Barnes", "Sandoval", "Mann", "Smith", "Davis", "Thomas", "Chavez", "Jennings", "West", "Moore", "Hernandez", "Tate", "Torres", "Morris", "Barajas", "Gomez", "Walker", "Long", "Aguirre", "Ellis", "Gray", "Haynes", "Bailey", "Chang", "Reed", "Price", "Williams"};
	public String[] currencies = {"USD", "EUR", "JPY", "AUD", "RMB", "HKD", "GBP", "CHF", "SGD"};
	public String genStockCode() {
		String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String numer = "0123456789";
		return alpha.charAt(rand.nextInt(26)) + "" + alpha.charAt(rand.nextInt(10)) + "" + alpha.charAt(rand.nextInt(26)) + "" + numer.charAt(rand.nextInt(10)) + alpha.charAt(rand.nextInt(26)) + numer.charAt(rand.nextInt(10));
	}
	
	public String genStockName() {
		return firstNames[rand.nextInt(firstNames.length)] + " & " + firstNames[rand.nextInt(firstNames.length)];
	}
	
	public String genCurrency() {
		return currencies[rand.nextInt(currencies.length)];
	}
	
	public double genPrice(AssetClass c) {
		double d = ValueRound.RoundTo(rand.nextDouble(), 2);
		switch(c) {
			case A1:
				return rand.nextInt(50,100) + d;
			case A2:
				return rand.nextInt(10, 50) + d;
			case A3:
				return rand.nextInt(10) + d;
			default:
				return d;
		}
	}
	
	public int genIPOYear() {
		return rand.nextInt(1990, 2019);
	}
}
