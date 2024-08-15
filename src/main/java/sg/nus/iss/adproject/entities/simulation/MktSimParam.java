package sg.nus.iss.adproject.entities.simulation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import jakarta.persistence.*;
import sg.nus.iss.adproject.entities.AssetClass;

@Entity
public class MktSimParam {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Transient
	private Random rand;
	
	private LocalDate paramDateEffective;
	
	private LocalDateTime paramDateCreated;
	
	private String paramCreatedBy;
	
	private float a1PVRLwrBd;
	
	private float a1PVRUprBd;
	
    private float a2PVRLwrBd;
	
	private float a2PVRUprBd;
	
	private float a3PVRLwrBd;
	
	private float a3PVRUprBd;
	
	private int a1TVUprBd;
	
	private int a1TVLwrBd;
	
	private int a2TVUprBd;
	
	private int a2TVLwrBd;
	
	private int a3TVUprBd;
	
	private int a3TVLwrBd;
	
	private int a1TCCap;
	
	private int a2TCCap;
	
	private int a3TCCap;
	
	
	public MktSimParam(LocalDate paramDateEffective,
			int classA1PriceVariationRatioLowerBound, int classA1PriceVariationRatioUpperBound,
			int classA2PriceVariationRatioLowerBound, int classA2PriceVariationRatioUpperBound,
			int classA3PriceVariationRatioLowerBound, int classA3PriceVariationRatioUpperBound,
			int classA1TradeVolumeLowerBound, int classA1TradeVolumeUpperBound, int classA2TradeVolumeLowerBound, int classA2TradeVolumeUpperBound,
			int classA3TradeVolumeLowerBound, int classA3TradeVolumeUpperBound,
			int classA1TradeCountCap, int classA2TradeCountCap, int classA3TradeCountCap) {
		super();
		this.paramDateEffective = paramDateEffective;
		this.paramDateCreated = LocalDateTime.now();
		this.paramCreatedBy = "admin";
		this.a1PVRLwrBd = classA1PriceVariationRatioLowerBound;
		this.a1PVRUprBd = classA1PriceVariationRatioUpperBound;
		this.a2PVRLwrBd = classA2PriceVariationRatioLowerBound;
		this.a2PVRUprBd = classA2PriceVariationRatioUpperBound;
		this.a3PVRLwrBd = classA3PriceVariationRatioLowerBound;
		this.a3PVRUprBd = classA3PriceVariationRatioUpperBound;
		this.a1TVUprBd = classA1TradeVolumeUpperBound;
		this.a1TVLwrBd = classA1TradeVolumeLowerBound;
		this.a2TVUprBd = classA2TradeVolumeUpperBound;
		this.a2TVLwrBd = classA2TradeVolumeLowerBound;
		this.a3TVUprBd = classA3TradeVolumeUpperBound;
		this.a3TVLwrBd = classA3TradeVolumeLowerBound;
		this.a1TCCap = classA1TradeCountCap;
		this.a2TCCap = classA2TradeCountCap;
		this.a3TCCap = classA3TradeCountCap;
		this.rand = new Random();
	}

	public MktSimParam() {
		this.paramDateCreated = LocalDateTime.now();
		this.paramCreatedBy = "admin";
		this.rand = new Random();
	}
	
	public void setSeed(long seed) {
		rand.setSeed(seed);
	}
	
	public double genPriceVariation(AssetClass c) {
		// use model to predict up or down
		int sign = rand.nextDouble() > (0.5) ? -1 : 1;
		switch(c) {
		case A1:
			return (1 + sign * rand.nextDouble(a1PVRLwrBd, a1PVRUprBd) / 100.0);
		case A2:
			return (1 + sign * rand.nextDouble(a2PVRLwrBd, a2PVRUprBd) / 100.0);
		case A3:
			return (1 + sign * rand.nextDouble(a3PVRLwrBd, a3PVRUprBd) / 100.0);
		default:
			return 1.0;
		}
	}
	
	public int genTradeVolume(AssetClass c) {
		switch(c) {
		case A1:
			return rand.nextInt(a1TVLwrBd, a1TVUprBd);
		case A2:
			return rand.nextInt(a2TVLwrBd, a2TVUprBd);
		case A3:
			return rand.nextInt(a3TVLwrBd, a3TVUprBd);
		default:
			return 0;
		}
	}
	
	public int genTradeCount(AssetClass c) {
		switch(c) {
		case A1:
			return rand.nextInt(a1TCCap + 1);
		case A2:
			return rand.nextInt(a2TCCap + 1);
		case A3:
			return rand.nextInt(a3TCCap + 1);
		default:
			return 0;
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getParamDateEffective() {
		return paramDateEffective;
	}

	public void setParamDateEffective(LocalDate paramDateEffective) {
		this.paramDateEffective = paramDateEffective;
	}

	public LocalDateTime getParamDateCreated() {
		return paramDateCreated;
	}

	public void setParamDateCreated(LocalDateTime paramDateCreated) {
		this.paramDateCreated = paramDateCreated;
	}

	public String getParamCreatedBy() {
		return paramCreatedBy;
	}

	public void setParamCreatedBy(String paramCreatedBy) {
		this.paramCreatedBy = paramCreatedBy;
	}

	public float getA1PVRLwrBd() {
		return a1PVRLwrBd;
	}

	public void setA1PVRLwrBd(float a1pvrLwrBd) {
		a1PVRLwrBd = a1pvrLwrBd;
	}

	public float getA1PVRUprBd() {
		return a1PVRUprBd;
	}

	public void setA1PVRUprBd(float a1pvrUprBd) {
		a1PVRUprBd = a1pvrUprBd;
	}

	public float getA2PVRLwrBd() {
		return a2PVRLwrBd;
	}

	public void setA2PVRLwrBd(float a2pvrLwrBd) {
		a2PVRLwrBd = a2pvrLwrBd;
	}

	public float getA2PVRUprBd() {
		return a2PVRUprBd;
	}

	public void setA2PVRUprBd(float a2pvrUprBd) {
		a2PVRUprBd = a2pvrUprBd;
	}

	public float getA3PVRLwrBd() {
		return a3PVRLwrBd;
	}

	public void setA3PVRLwrBd(float a3pvrLwrBd) {
		a3PVRLwrBd = a3pvrLwrBd;
	}

	public float getA3PVRUprBd() {
		return a3PVRUprBd;
	}

	public void setA3PVRUprBd(float a3pvrUprBd) {
		a3PVRUprBd = a3pvrUprBd;
	}

	public int getA1TVUprBd() {
		return a1TVUprBd;
	}

	public void setA1TVUprBd(int a1tvUprBd) {
		a1TVUprBd = a1tvUprBd;
	}

	public int getA1TVLwrBd() {
		return a1TVLwrBd;
	}

	public void setA1TVLwrBd(int a1tvLwrBd) {
		a1TVLwrBd = a1tvLwrBd;
	}

	public int getA2TVUprBd() {
		return a2TVUprBd;
	}

	public void setA2TVUprBd(int a2tvUprBd) {
		a2TVUprBd = a2tvUprBd;
	}

	public int getA2TVLwrBd() {
		return a2TVLwrBd;
	}

	public void setA2TVLwrBd(int a2tvLwrBd) {
		a2TVLwrBd = a2tvLwrBd;
	}

	public int getA3TVUprBd() {
		return a3TVUprBd;
	}

	public void setA3TVUprBd(int a3tvUprBd) {
		a3TVUprBd = a3tvUprBd;
	}

	public int getA3TVLwrBd() {
		return a3TVLwrBd;
	}

	public void setA3TVLwrBd(int a3tvLwrBd) {
		a3TVLwrBd = a3tvLwrBd;
	}

	public int getA1TCCap() {
		return a1TCCap;
	}

	public void setA1TCCap(int a1tcCap) {
		a1TCCap = a1tcCap;
	}

	public int getA2TCCap() {
		return a2TCCap;
	}

	public void setA2TCCap(int a2tcCap) {
		a2TCCap = a2tcCap;
	}

	public int getA3TCCap() {
		return a3TCCap;
	}

	public void setA3TCCap(int a3tcCap) {
		a3TCCap = a3tcCap;
	}
	
	public static MktSimParam initialMktSimParam() {
		MktSimParam initialParam = new MktSimParam(LocalDate.of(2024, 7, 30), 1, 3, 1, 5, 1, 7, 1, 2, 1, 3, 1, 4, 1, 1, 3);
		initialParam.setParamCreatedBy("admin");
		initialParam.setParamDateCreated(LocalDateTime.now());
		return initialParam;
	}
}
