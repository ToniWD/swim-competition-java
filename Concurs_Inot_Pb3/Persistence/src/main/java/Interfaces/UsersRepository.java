package Interfaces;

import Models.User;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 */
public interface UsersRepository extends Repository<Long, User> {

    /**
     * Returns the user with the specified username.
     *
     * @param username the username associated with the user
     * @return an {@code Optional} containing the {@link User}, if found
     * @throws IllegalArgumentException if {@code username} is {@code null}
     */
    Optional<User> findByUsername(String username);
}
