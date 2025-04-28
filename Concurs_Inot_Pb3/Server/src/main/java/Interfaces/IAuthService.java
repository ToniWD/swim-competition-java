package Interfaces;

import Utils.ServiceException;

public interface IAuthService {

    boolean authentificate(final String username, final String password) throws ServiceException;

}
