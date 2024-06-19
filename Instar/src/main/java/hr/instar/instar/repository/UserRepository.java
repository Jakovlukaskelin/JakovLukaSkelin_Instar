package hr.instar.instar.repository;

import hr.instar.instar.doamin.users;

public interface UserRepository {

    boolean userExists(String username);
    void registerUser(String username, String password);
    users findByUsername(String username);

}
