package com.kltn.authservice.business.user;


public interface UserService {

    User findByCodeIgnoreCase(String code);

    User createUser(User user);
}
