package sg.nus.iss.adproject.entities.learning;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "sensitives")
public class Sensitive implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
