package Interfaces;

import Utils.ServiceException;

/**
 * Defines authentication services for user login.
 */
public interface IAuthService {

    /**
     * Authenticates a user based on username and password.
     *
     * @param username the existing username
     * @param password the corresponding password
     * @return {@code true} if the credentials are valid
     * @throws ServiceException if the username or password is {@code null},
     *                          or if no user is associated with the username
     */
    boolean authenticate(final String username, final String password) throws ServiceException;
}
