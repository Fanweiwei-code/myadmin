package com.fan.myadmin.service;

import com.fan.myadmin.entity.User;
import com.fan.myadmin.exception.EntityNotFoundException;
import com.fan.myadmin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fanweiwei
 * @create 2020-04-01 21:54
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        User user;

        user = userRepository.findByUsername(username);

        if (user == null) {
            throw new EntityNotFoundException(User.class, "name", username);
        } else {
            return user;
        }
    }

}
