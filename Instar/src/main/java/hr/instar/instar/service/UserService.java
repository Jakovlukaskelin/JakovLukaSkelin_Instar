package hr.instar.instar.service;

import hr.instar.instar.doamin.users;
import hr.instar.instar.repository.StoreRepository;
import hr.instar.instar.repository.UserRepository;
import lombok.AllArgsConstructor;




import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
   private final StoreRepository storeRepository;
    private PasswordEncoder passwordEncoder;

    public void registerUser(String username, String password) {
        if (!userRepository.userExists(username)) {
            String encodedPassword = passwordEncoder.encode(password);
            userRepository.registerUser(username, encodedPassword);
        } else {
            throw new IllegalArgumentException("User already exists with username: " + username);
        }
    }
    public users loginUser(String username, String password) {
        if (userRepository.userExists(username)) {
            users user = userRepository.findByUsername(username);
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
        }
    public void deleteUser(String username) {
        storeRepository.deleteUser(username);
    }

    public List<users> getAllUsers() {
        return storeRepository.getAllUsers();
    }

}