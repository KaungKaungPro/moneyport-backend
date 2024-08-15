package sg.nus.iss.adproject.services.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.adproject.entities.learning.Sensitive;
import sg.nus.iss.adproject.repositories.learning.SensitiveRepository;

import java.util.List;


@Service
@Transactional
public class SensitiveService implements SensitiveInterface {
    @Autowired
    private SensitiveRepository repository;

    @Override
    public boolean has(String text) {
        List<Sensitive> list = repository.findSensitiveByContent(text);
        return !list.isEmpty();
    }
}
