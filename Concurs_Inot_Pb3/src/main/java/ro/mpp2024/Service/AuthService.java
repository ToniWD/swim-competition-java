package ro.mpp2024.Service;

import ro.mpp2024.Domain.User;
import ro.mpp2024.Repository.DBRepositories.UsersDBRepo;
import ro.mpp2024.Repository.Interfaces.UsersRepository;
import ro.mpp2024.Service.Interfaces.IAuthService;

import java.util.Optional;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthService implements IAuthService {

    private static final Logger logger = LogManager.getLogger(AuthService.class);
    private UsersRepository usersRepo;

    public AuthService(UsersRepository usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public boolean authentificate(String username, String password) throws ServiceException {
        logger.info("Authenticating user " + username);

        if(username == null || username.isEmpty()) {
            throw new ServiceException("Password cannot be null or empty");
        }

        if(password == null || password.isEmpty()) {
            throw new ServiceException("Password cannot be null or empty");
        }

        Optional<User> user = usersRepo.findByUsername(username);

        if (user.isPresent()) {
            return user.get().getPassword().equals(password);
        }
        else {
            logger.warn("User " + username + " not found");
            throw new ServiceException("User not found");
        }
    }

}
