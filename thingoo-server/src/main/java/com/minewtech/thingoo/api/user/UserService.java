package com.minewtech.thingoo.api.user;

import com.minewtech.thingoo.model.user.User;
import com.minewtech.thingoo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public String getLoggedInUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null){
            return "nosession";
        }
        return auth.getName();
    }


    public User getLoggedInUser() {
        String loggedInUserId = this.getLoggedInUserId();
       // System.out.format("\n1. Inside >> getLoggedInUser: %s", loggedInUserId);
        User user = this.getUserByUuid(loggedInUserId);
        //System.out.format("\n2. After Find User: %s", loggedInUserId);
        return user;
    }

    public User getUserByUuid(String uuid){
        User user = this.userRepository.findOneByUuid(uuid).orElseGet( () -> new User());
        return user;
    }
}
