package sg.nus.iss.adproject.services.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.adproject.entities.learning.*;
import sg.nus.iss.adproject.repositories.learning.FriendLinkRepository;

import java.util.List;


@Service
@Transactional
public class FriendLinkService implements FriendLinkInterface {
    @Autowired
    private FriendLinkRepository repository;
    @Override
    public List<FriendLink> getAllFriendLink() {
        List<FriendLink> list = repository.findAll();
        return list;
    }
}
