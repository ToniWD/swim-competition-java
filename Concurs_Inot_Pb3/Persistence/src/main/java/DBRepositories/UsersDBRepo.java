package DBRepositories;
import Models.User;
import Utils.JdbcUtils;
import Utils.RepoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Interfaces.UsersRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class UsersDBRepo implements UsersRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public UsersDBRepo(Properties props) {
        logger.info("Initializing UserDBRepo with properties: {} ", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        logger.traceEntry();

        User user = null;
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "SELECT id, password FROM users where username = ?"
        ))
        {
            preStmt.setString(1, username);
            ResultSet resultSet = preStmt.executeQuery();

            if(resultSet.next()){
                Long id = resultSet.getLong("id");
                String password = resultSet.getString("password");

                user = new User(username, password);
                user.setId(id);
            }
        }
        catch (SQLException ex) {
            logger.error(ex);
        }
        logger.traceExit();

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findOne(Long id) {
        logger.traceEntry();

        if(id == null) {
            logger.error("ID cannot be null");
            throw new RepoException("ID cannot be null");
        }

        User user = null;
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "SELECT username, password FROM users where id = ?"
        ))
        {
            preStmt.setLong(1,id);
            ResultSet resultSet = preStmt.executeQuery();

            if(resultSet.next()){
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                user = new User(username, password);
                user.setId(id);
            }
        }
        catch (SQLException ex) {
            logger.error(ex);
        }
        logger.traceExit();

        return Optional.ofNullable(user);
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry();

        Connection con=dbUtils.getConnection();
        List<User> users = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement("select id, username, password from users")){
            try(ResultSet result = preStmt.executeQuery()){
                while (result.next()){
                    Long id = result.getLong("id");
                    String username = result.getString("username");
                    String password = result.getString("password");

                    User user = new User(username, password);
                    user.setId(id);
                    users.add(user);
                }
            }

        }
        catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
        return users;
    }

    @Override
    public Optional<User> save(User entity) {
        logger.traceEntry("Saving task {} ", entity);

        if(entity == null) {
            logger.error("Models.Entity cannot be null");
            throw new RepoException("Models.Entity cannot be null");
        }

        Connection con=dbUtils.getConnection();

        int result = -1;

        try(PreparedStatement preStmt =
                    con.prepareStatement("insert into users (username, password) values (?,?)")){

            preStmt.setString(1,entity.getUsername());
            preStmt.setString(2, entity.getPassword());

            result = preStmt.executeUpdate();

            logger.trace("Saved {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex.getMessage());
            throw new RepoException("Can't save user");
        }
        logger.traceExit();

        if(result > 0){
            return Optional.empty();
        }
        else {
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<User> delete(Long id) {

        logger.traceEntry("Deleting task {} ", id);

        if(id == null) {
            logger.error("ID cannot be null");
            throw new RepoException("ID cannot be null");
        }

        Connection con = dbUtils.getConnection();

        Optional<User> user = this.findOne(id);

        if (user.isPresent()) {
            try(PreparedStatement preStmt =
                        con.prepareStatement("DELETE FROM users WHERE id = ?;\n"))
            {
                preStmt.setLong(1, id);

                int result = preStmt.executeUpdate();

                logger.trace("Deleted {} instances", result);
            }
            catch (SQLException ex){
                logger.error(ex.getMessage());
            }
        }
        logger.traceExit();

        return user;
    }

    @Override
    public Optional<User> update(User entity) {
        return Optional.empty();
    }


}
