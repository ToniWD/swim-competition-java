package ro.mpp2024.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Participant;
import ro.mpp2024.Domain.Record;
import ro.mpp2024.Domain.SwimmingEvent;
import ro.mpp2024.Domain.Validators.ParticipantValidator;
import ro.mpp2024.Repository.DBRepositories.ParticipantsDBRepo;
import ro.mpp2024.Repository.DBRepositories.RecordsDBRepo;
import ro.mpp2024.Repository.DBRepositories.SwimmingEventsDBRepo;
import ro.mpp2024.Repository.Interfaces.ParticipantsRepository;
import ro.mpp2024.Repository.Interfaces.RecordsRepository;
import ro.mpp2024.Repository.Interfaces.SwimmingEventsRepository;
import ro.mpp2024.Repository.RepoException;
import ro.mpp2024.Service.Interfaces.IMainService;

import java.util.Properties;

public class MainService implements IMainService {

    private static final Logger logger = LogManager.getLogger(MainService.class);
    private final SwimmingEventsRepository swimmingEventsRepository;
    private final ParticipantsRepository participantsRepository;
    private final RecordsRepository recordsRepository;

    public MainService(ParticipantsRepository participantsRepository, SwimmingEventsRepository swimmingEventsRepository, RecordsRepository recordsRepository) {
        this.participantsRepository = participantsRepository;
        this.swimmingEventsRepository = swimmingEventsRepository;
        this.recordsRepository = recordsRepository;
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
    }
}
