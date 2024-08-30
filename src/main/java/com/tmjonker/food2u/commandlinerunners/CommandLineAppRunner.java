package com.tmjonker.food2u.commandlinerunners;

import com.tmjonker.food2u.entities.user.User;
import com.tmjonker.food2u.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppRunner implements CommandLineRunner {

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    @Autowired
    public CommandLineAppRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // initializes admin account with a default password of "password" when server is first started.
        // Password must be changed upon initial login by admin user.  DO THIS FIRST THING.
        if (!userRepository.existsByEmail("admin@food2u.com")) {
            User adminUser = new User("admin@food2u.com", passwordEncoder.encode("password"), "tim", "m"
                    , "jonker", "123 Main st.", "n/a", "manassas", "VA", "20110", "777-777-7777");
            userRepository.save(adminUser);
        }
    }
}
