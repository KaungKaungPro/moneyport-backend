package sg.nus.iss.adproject.entities.learning;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
@Entity
@Table(name = "terminology")
public class Terminology implements Serializable {

    @Serial
    private static final long serialVersionUID = 1336531213644521235L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "term")
    private String term;

    @Column(name = "definition")
    private String definition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
