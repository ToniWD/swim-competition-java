package Services;

import Interfaces.*;
import Models.Participant;
import Models.SwimmingEvent;
import Models.User;
import Utils.RepoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Models.Record;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MainService implements IMainService {

    private static final Logger logger = LogManager.getLogger(MainService.class);
    private final SwimmingEventsRepository swimmingEventsRepository;
    private final ParticipantsRepository participantsRepository;
    private final RecordsRepository recordsRepository;

    private final Map<String, IObserver> loggedClients;

    public MainService(ParticipantsRepository participantsRepository, SwimmingEventsRepository swimmingEventsRepository, RecordsRepository recordsRepository) {
        this.participantsRepository = participantsRepository;
        this.swimmingEventsRepository = swimmingEventsRepository;
        this.recordsRepository = recordsRepository;
        loggedClients = new ConcurrentHashMap<>();
    }


    @Override
    public Iterable<SwimmingEvent> getSwimmingEvents() {
        return swimmingEventsRepository.findAll();
    }

    @Override
    public Iterable<Participant> getParticipantsByEvent(SwimmingEvent event) {
        return participantsRepository.getParticipantsByEvent(event.getId());
    }

    @Override
    public Iterable<Participant> getParticipantsByEvent(SwimmingEvent event, String fullName) {
        fullName = fullName.trim().replaceAll("\\s+", " ");
        return participantsRepository.getParticipantsByEvent(event.getId(), fullName);
    }

    @Override
    public void addParticipant(String firstName, String lastName, int age, Iterable<SwimmingEvent> swimmingEvents) {
        Participant participant = new Participant(firstName, lastName, age);

        participantsRepository.save(participant);

        swimmingEvents.forEach(x ->{
            Record record = new Record(participant, x);
            try {
                recordsRepository.save(record);
            }
            catch (RepoException ex){
                logger.error(ex);

            }
            catch (Exception e) {
                logger.error(e);
            }
        });
        notifyClients();
    }

    @Override
    public void addClient(User user, IObserver client) {

        loggedClients.put(user.getUsername(), client);
        logger.debug("Clients adaugati {}", loggedClients.keySet());
    }

    @Override
    public IObserver removeClient(User user) {
        return loggedClients.remove(user.getUsername());
    }


    public void notifyClients(){
        logger.info("Clients {}", loggedClients.keySet());
        loggedClients.forEach((k,v)->{
            logger.info("Notify user {}", k);
            v.Update();});
    }
}
