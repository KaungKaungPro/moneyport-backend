package sg.nus.iss.adproject.entities.learning;

import jakarta.persistence.*;

@Entity
public class InvestTerm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String definition;
	
	private String url;

	public InvestTerm(Long id, String name, String definition, String url) {
		super();
		this.id = id;
		this.name = name;
		this.definition = definition;
		this.url = url;
	}
	
	public InvestTerm() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
}

