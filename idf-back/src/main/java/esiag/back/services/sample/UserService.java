package esiag.back.services.sample;

import esiag.back.models.sample.User;
import esiag.back.repositories.sample.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> authenticate(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User addUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}