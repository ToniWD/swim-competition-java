package Utils;

import Models.User;

/**
 * Provides methods for password encryption and verification for {@link User} objects.
 */
public interface IEncodeUser {

    /**
     * Encrypts the user's plain-text password and updates the user object with the encrypted version.
     *
     * @param user the {@link User} whose password is in plain text and will be encrypted
     */
    void encryptPassword(User user);

    /**
     * Verifies whether the provided plain-text password matches the user's encrypted password.
     *
     * @param user     the {@link User} whose password is already encrypted
     * @param password the plain-text password to validate
     * @return {@code true} if the password matches; {@code false} otherwise
     */
    boolean verifyPassword(User user, String password);
}
