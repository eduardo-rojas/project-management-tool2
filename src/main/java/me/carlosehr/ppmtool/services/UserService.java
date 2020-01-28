package me.carlosehr.ppmtool.services;

import me.carlosehr.ppmtool.domain.User;
import me.carlosehr.ppmtool.exceptions.UsernameAlreadyExistsException;
import me.carlosehr.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser (User newUser){

        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //Username has to be unique(Exception)
            newUser.setUsername((newUser.getUsername()));

            //Make sure that password and confirmPassword match
            //We don't persist or show the confirmPassword
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);

        }catch(Exception e){
            throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");

        }

    }

}
