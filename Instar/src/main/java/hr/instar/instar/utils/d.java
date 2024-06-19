package hr.instar.instar.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class d {

    public static void main(String[] args) {
        String password = "password";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println(hashedPassword);
    }
}
