package Utils;

import Models.User;

public interface IEncodeUser {

    void encryptPassword(User user);

    boolean verifyPassword(User user, String password);
}
