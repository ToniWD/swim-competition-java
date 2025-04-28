package Interfaces;

import Models.User;

import java.util.Optional;

public interface UsersRepository extends Repository<Long, User>{

    Optional<User> findByUsername(String username);
}
