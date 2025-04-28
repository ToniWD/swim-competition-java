package Utils;

import Models.User;
import org.mindrot.jbcrypt.BCrypt;

public class EncodeUserBcrypt implements IEncodeUser {
    @Override
    public void encryptPassword(User user) {
        String pwd = user.getUsername() + user.getPassword();

        user.setPassword(BCrypt.hashpw(pwd, BCrypt.gensalt()));
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        String pwd = user.getUsername() + password;

        return BCrypt.checkpw(pwd, user.getPassword());
    }
}
