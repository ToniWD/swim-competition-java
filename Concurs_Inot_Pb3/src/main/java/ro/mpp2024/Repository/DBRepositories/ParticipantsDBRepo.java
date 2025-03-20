package ro.mpp2024.Repository.DBRepositories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Participant;
import ro.mpp2024.Repository.Interfaces.ParticipantsRepository;
import ro.mpp2024.Repository.Interfaces.Repository;
import ro.mpp2024.Repository.JdbcUtils;
import ro.mpp2024.Repository.RepoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ParticipantsDBRepo implements ParticipantsRepository {

    private final JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public ParticipantsDBRepo(Properties props) {
        logger.info("Initializing ParticipantsDBRepo with properties: {} ", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Participant> findOne(Long id) {
        logger.traceEntry();

        if(id == null) {
            logger.error("ID cannot be null");
            throw new RepoException("ID cannot be null");
        }
        
        Participant participant = null;
        Connection con = dbUtils.getConnection();
        
        try (PreparedStatement preStmt = con.prepareStatement(
                     "SELECT first_name, last_name, age FROM participants where id = ?"
             ))
        {
            preStmt.setLong(1,id);
            ResultSet resultSet = preStmt.executeQuery();

            if(resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int age = resultSet.getInt("age");

                participant = new Participant(firstName, lastName, age);
                participant.setId(id);
            }
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();

        return Optional.ofNullable(participant);
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry();

        Connection con=dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement("select id, first_name, last_name, age from participants")){
            try(ResultSet result = preStmt.executeQuery()){
                while (result.next()){
                    Long id = result.getLong("id");
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    int age = result.getInt("age");

                    Participant participant = new Participant(firstName, lastName, age);
                    participant.setId(id);
                    participants.add(participant);
                }
            }

        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();
        return participants;
    }

    @Override
    public Optional<Participant> save(Participant entity) {
        logger.traceEntry("Saving task {} ", entity);

        if(entity == null) {
            logger.error("Entity cannot be null");
            throw new RepoException("Entity cannot be null");
        }

        Connection con=dbUtils.getConnection();

        int result = -1;

        try(PreparedStatement preStmt =
                    con.prepareStatement("insert into participants (first_name, last_name, age) values (?,?,?)")){

            preStmt.setString(1,entity.getFirstName());
            preStmt.setString(2, entity.getLastName());
            preStmt.setInt(3,entity.getAge());

            result = preStmt.executeUpdate();

            logger.trace("Saved {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex.getMessage());
            System.err.println("Error DB"+ex);
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
    public Optional<Participant> delete(Long id) {

        logger.traceEntry("Deleting task {} ", id);

        if(id == null) {
            logger.error("ID cannot be null");
            throw new RepoException("ID cannot be null");
        }

        Connection con = dbUtils.getConnection();

        Optional<Participant> participant = this.findOne(id);

        if (participant.isPresent()) {
            try(PreparedStatement preStmt =
                        con.prepareStatement("DELETE FROM participants WHERE id = ?;\n"))
            {
                preStmt.setLong(1, id);

                int result = preStmt.executeUpdate();

                logger.trace("Deleted {} instances", result);
            }
            catch (SQLException ex){
                logger.error(ex.getMessage());
                System.err.println("Error DB"+ex);
            }
        }
        logger.traceExit();

        return participant;
    }

    @Override
    public Optional<Participant> update(Participant entity) {
        return Optional.empty();
    }
}
