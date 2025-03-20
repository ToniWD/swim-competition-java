package ro.mpp2024.Repository.DBRepositories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.SwimmingEvent;
import ro.mpp2024.Repository.Interfaces.Repository;
import ro.mpp2024.Repository.Interfaces.SwimmingEventsRepository;
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

public class SwimmingEventsDBRepo implements SwimmingEventsRepository {

    private final JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public SwimmingEventsDBRepo(Properties props) {
        logger.info("Initializing SwimmingEventsDBRepo with properties: {} ", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<SwimmingEvent> findOne(Long id) {
        logger.traceEntry();

        if(id == null) {
            logger.error("ID cannot be null");
            throw new RepoException("ID cannot be null");
        }

        SwimmingEvent event = null;
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "SELECT distance, style FROM swimming_events where id = ?"
        ))
        {
            preStmt.setLong(1,id);
            ResultSet result = preStmt.executeQuery();

            if(result.next()){
                int distance = result.getInt("distance");
                String style = result.getString("style");

                event = new SwimmingEvent(distance, style);
                event.setId(id);
            }
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();

        return Optional.ofNullable(event);
    }

    @Override
    public Iterable<SwimmingEvent> findAll() {
        logger.traceEntry();

        Connection con=dbUtils.getConnection();
        List<SwimmingEvent> events = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement("select id, distance, style from swimming_events")){
            try(ResultSet result = preStmt.executeQuery()){
                while (result.next()){
                    Long id = result.getLong("id");
                    int distance = result.getInt("distance");
                    String style = result.getString("style");

                    SwimmingEvent event = new SwimmingEvent(distance, style);
                    event.setId(id);
                    events.add(event);
                }
            }

        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();
        return events;
    }

    @Override
    public Optional<SwimmingEvent> save(SwimmingEvent entity) {
        logger.traceEntry("Saving task {} ", entity);

        if(entity == null) {
            logger.error("Entity cannot be null");
            throw new RepoException("Entity cannot be null");
        }

        Connection con=dbUtils.getConnection();

        int result = -1;

        try(PreparedStatement preStmt =
                    con.prepareStatement("insert into swimming_events (distance, style) values (?,?)")){

            preStmt.setInt(1,entity.getDistance());
            preStmt.setString(2,entity.getStyle());

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
    public Optional<SwimmingEvent> delete(Long id) {

        logger.traceEntry("Deleting task {} ", id);

        if(id == null) {
            logger.error("ID cannot be null");
            throw new RepoException("ID cannot be null");
        }

        Connection con = dbUtils.getConnection();

        Optional<SwimmingEvent> event = this.findOne(id);

        if (event.isPresent()) {
            try(PreparedStatement preStmt =
                        con.prepareStatement("DELETE FROM swimming_events WHERE id = ?;\n"))
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

        return event;
    }

    @Override
    public Optional<SwimmingEvent> update(SwimmingEvent entity) {
        return Optional.empty();
    }
}
