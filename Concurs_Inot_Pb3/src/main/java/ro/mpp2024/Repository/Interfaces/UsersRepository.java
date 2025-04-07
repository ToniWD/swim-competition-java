package ro.mpp2024.Repository.Interfaces;

import ro.mpp2024.Domain.User;

import java.util.Optional;

public interface UsersRepository extends Repository<Long, User>{

    Optional<User> findByUsername(String username);
}
