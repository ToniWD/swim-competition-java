package Services;

import Interfaces.IObserver;
import Interfaces.UsersRepository;
import Models.User;
import Interfaces.IAuthService;

import java.util.Optional;

import Utils.EncodeUserBcrypt;
import Utils.IEncodeUser;
import Utils.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthService implements IAuthService {

    private static final Logger logger = LogManager.getLogger(AuthService.class);
    private UsersRepository usersRepo;
    private IEncodeUser encodeUser = new EncodeUserBcrypt();

    public AuthService(UsersRepository usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public boolean authenticate(String username, String password) throws ServiceException {
        logger.info("Authenticating user " + username);

        if(username == null || username.isEmpty()) {
            throw new ServiceException("Password cannot be null or empty");
        }

        if(password == null || password.isEmpty()) {
            throw new ServiceException("Password cannot be null or empty");
        }

        Optional<User> user = usersRepo.findByUsername(username);

        if (user.isPresent()) {
            return encodeUser.verifyPassword(user.get(), password);
        }
        else {
            logger.warn("Models.User " + username + " not found");
            throw new ServiceException("Models.User not found");
        }
    }

}
