package sg.nus.iss.adproject.services.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sg.nus.iss.adproject.entities.learning.*;
import sg.nus.iss.adproject.repositories.learning.TerminologyRepository;

import java.util.List;

@Service
@Transactional
public class TerminologyService implements TerminologyInterface {

    @Autowired
    private TerminologyRepository repository;

    @Override
    public List<Terminology> getAllTerminology(String term) {
        List<Terminology> list;
        if (StringUtils.hasText(term)) {
            list = repository.findByTermContaining(term);
        } else {
            list = repository.findAll();
        }
        return list;
    }
}
