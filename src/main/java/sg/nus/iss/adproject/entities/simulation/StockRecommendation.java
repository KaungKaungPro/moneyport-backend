package sg.nus.iss.adproject.entities.simulation;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
public class StockRecommendation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private LocalDate recommendationDate; 
    
    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Stock a1Stock1;
    
    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Stock a1Stock2;
    
    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Stock a1Stock3;
    
    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Stock a2Stock1;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Stock a2Stock2;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Stock a2Stock3;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Stock a3Stock1;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Stock a3Stock2;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Stock a3Stock3;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getRecommendationDate() {
		return recommendationDate;
	}

	public void setRecommendationDate(LocalDate recommendationDate) {
		this.recommendationDate = recommendationDate;
	}

	public Stock getA1Stock1() {
		return a1Stock1;
	}

	public void setA1Stock1(Stock a1Stock1) {
		this.a1Stock1 = a1Stock1;
	}

	public Stock getA1Stock2() {
		return a1Stock2;
	}

	public void setA1Stock2(Stock a1Stock2) {
		this.a1Stock2 = a1Stock2;
	}

	public Stock getA1Stock3() {
		return a1Stock3;
	}

	public void setA1Stock3(Stock a1Stock3) {
		this.a1Stock3 = a1Stock3;
	}

	public Stock getA2Stock1() {
		return a2Stock1;
	}

	public void setA2Stock1(Stock a2Stock1) {
		this.a2Stock1 = a2Stock1;
	}

	public Stock getA2Stock2() {
		return a2Stock2;
	}

	public void setA2Stock2(Stock a2Stock2) {
		this.a2Stock2 = a2Stock2;
	}

	public Stock getA2Stock3() {
		return a2Stock3;
	}

	public void setA2Stock3(Stock a2Stock3) {
		this.a2Stock3 = a2Stock3;
	}

	public Stock getA3Stock1() {
		return a3Stock1;
	}

	public void setA3Stock1(Stock a3Stock1) {
		this.a3Stock1 = a3Stock1;
	}

	public Stock getA3Stock2() {
		return a3Stock2;
	}

	public void setA3Stock2(Stock a3Stock2) {
		this.a3Stock2 = a3Stock2;
	}

	public Stock getA3Stock3() {
		return a3Stock3;
	}

	public void setA3Stock3(Stock a3Stock3) {
		this.a3Stock3 = a3Stock3;
	}

	
//	@OneToMany(mappedBy = "a1Recommendation", cascade = CascadeType.ALL)
//	private List<Stock> a1Recommendations;
//		
//	@OneToMany(mappedBy = "a2Recommendation", cascade = CascadeType.ALL)
//	private List<Stock> a2Recommendations;
//		
//	@OneToMany(mappedBy = "a3Recommendation", cascade = CascadeType.ALL)
//	private List<Stock> a3Recommendations;
	
//    private List<Stock> a1Recommendations;
//
//    private List<Stock> a2Recommendations;
//
//    private List<Stock> a3Recommendations;
	
//	@OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL)
//    private List<Stock> a1Recommendations;
//
//    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL)
//    private List<Stock> a2Recommendations;
//
//    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL)
//    private List<Stock> a3Recommendations;
	        
//	public List<Stock> getA1Recommendations() {
//		return a1Recommendations;
//	}
//	
//	public void setA1Recommendations(List<Stock> a1Recommendations) {
//		this.a1Recommendations = a1Recommendations;
//	}
//	
//	public List<Stock> getA2Recommendations() {
//		return a2Recommendations;
//	}
//	
//	public void setA2Recommendations(List<Stock> a2Recommendations) {
//		this.a2Recommendations = a2Recommendations;
//	}
//	
//	public List<Stock> getA3Recommendations() {
//		return a3Recommendations;
//	}
//	
//	public void setA3Recommendations(List<Stock> a3Recommendations) {
//		this.a3Recommendations = a3Recommendations;
//	}
//	
//	public LocalDate getRecommendationDate() {
//		return recommendationDate;
//	}
//	
//	public void setRecommendationDate(LocalDate recommendationDate) {
//		this.recommendationDate = recommendationDate;
//	}	
}
