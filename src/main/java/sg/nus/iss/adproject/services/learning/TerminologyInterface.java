package sg.nus.iss.adproject.services.learning;

import sg.nus.iss.adproject.entities.learning.Terminology;

import java.util.List;

public interface TerminologyInterface {

    List<Terminology> getAllTerminology(String term);
}
