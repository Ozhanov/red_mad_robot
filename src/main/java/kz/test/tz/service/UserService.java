package kz.test.tz.service;

import kz.test.tz.entity.User;
import kz.test.tz.pogo.NewUserRequest;

import java.util.List;
import java.util.Map;

public interface UserService {
    User registry(NewUserRequest request);

    User findByEmail(String email);

    void delete(Long id);
}