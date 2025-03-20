package ro.mpp2024.Repository.DBRepositories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Participant;
import ro.mpp2024.Domain.Record;
import ro.mpp2024.Domain.SwimmingEvent;
import ro.mpp2024.Repository.Interfaces.RecordsRepository;
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

public class RecordsDBRepo implements RecordsRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public RecordsDBRepo(Properties props) {
        logger.info("Initializing ParticipantsDBRepo with properties: {} ", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Record> findOne(Long id) {
        logger.traceEntry();

        if(id == null) {
            logger.error("ID cannot be null");
            throw new RepoException("ID cannot be null");
        }

        Record record = null;
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "SELECT\n" +
                        "    records.id AS id_record,\n" +
                        "    participants.id AS id_participant,\n" +
                        "    participants.first_name,\n" +
                        "    participants.last_name,\n" +
                        "    participants.age,\n" +
                        "    swimming_events.id AS id_swimming_event,\n" +
                        "    swimming_events.distance,\n" +
                        "    swimming_events.style\n" +
                        "FROM records\n" +
                        "         JOIN participants ON records.id_participant = participants.id\n" +
                        "         JOIN swimming_events ON records.id_event = swimming_events.id\n" + 
                        "WHERE records.id = ?"
                
        ))
        {
            preStmt.setLong(1,id);
            ResultSet resultSet = preStmt.executeQuery();

            if(resultSet.next()){
                //Participant
                Long participantId = resultSet.getLong("id_participant");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int age = resultSet.getInt("age");
                
                Participant participant = new Participant(firstName, lastName, age);
                participant.setId(participantId);
                
                //Event
                Long eventId = resultSet.getLong("id_swimming_event");
                int distance = resultSet.getInt("distance");
                String style = resultSet.getString("style");

                SwimmingEvent event = new SwimmingEvent(distance, style);
                event.setId(eventId);
                //Record
                
                record = new Record(participant, event);
                record.setId(id);
            }
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();

        return Optional.ofNullable(record);
    }

    @Override
    public Iterable<Record> findAll() {
        logger.traceEntry();

        Connection con=dbUtils.getConnection();
        List<Record> records = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement(
                "SELECT\n" +
                        "    records.id AS id_record,\n" +
                        "    participants.id AS id_participant,\n" +
                        "    participants.first_name,\n" +
                        "    participants.last_name,\n" +
                        "    participants.age,\n" +
                        "    swimming_events.id AS id_swimming_event,\n" +
                        "    swimming_events.distance,\n" +
                        "    swimming_events.style\n" +
                        "FROM records\n" +
                        "         JOIN participants ON records.id_participant = participants.id\n" +
                        "         JOIN swimming_events ON records.id_event = swimming_events.id;"
        )){
            try(ResultSet result = preStmt.executeQuery()){
                while (result.next()){
                    //Participant
                    Long participantId = result.getLong("id_participant");
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    int age = result.getInt("age");

                    Participant participant = new Participant(firstName, lastName, age);
                    participant.setId(participantId);

                    //Event
                    Long eventId = result.getLong("id_swimming_event");
                    int distance = result.getInt("distance");
                    String style = result.getString("style");

                    SwimmingEvent event = new SwimmingEvent(distance, style);
                    event.setId(eventId);
                    //Record
                    Long id = result.getLong("id_record");

                    Record record = new Record(participant, event);
                    record.setId(id);
                    records.add(record);
                }
            }

        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();
        return records;
    }

    @Override
    public Optional<Record> save(Record entity) {
        logger.traceEntry("Saving task {} ", entity);

        if(entity == null) {
            logger.error("Entity cannot be null");
            throw new RepoException("Entity cannot be null");
        }

        Connection con=dbUtils.getConnection();

        int result = -1;

        try(PreparedStatement preStmt =
                    con.prepareStatement("INSERT INTO records (id_participant, id_event) VALUES (?,?)")){

            preStmt.setLong(1,entity.getParticipant().getId());
            preStmt.setLong(2, entity.getSwimmingEvent().getId());

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
    public Optional<Record> delete(Long id) {

        logger.traceEntry("Deleting task {} ", id);

        if(id == null) {
            logger.error("ID cannot be null");
            throw new RepoException("ID cannot be null");
        }

        Connection con = dbUtils.getConnection();

        Optional<Record> record = this.findOne(id);

        if (record.isPresent()) {
            try(PreparedStatement preStmt =
                        con.prepareStatement("DELETE FROM records WHERE id = ?;\n"))
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

        return record;
    }

    @Override
    public Optional<Record> update(Record entity) {
        return Optional.empty();
    }
}

