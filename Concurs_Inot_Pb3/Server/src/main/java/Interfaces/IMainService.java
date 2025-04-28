package Interfaces;



import Models.Participant;
import Models.SwimmingEvent;
import Models.User;
import Utils.ServiceException;

public interface IMainService {

    Iterable<SwimmingEvent> getSwimmingEvents() throws ServiceException;

    Iterable<Participant> getParticipantsByEvent(SwimmingEvent event) throws ServiceException;

    Iterable<Participant> getParticipantsByEvent(SwimmingEvent event, String fullName) throws ServiceException;

    void addParticipant(String firstName, String lastName, int age, Iterable<SwimmingEvent> swimmingEvents) throws ServiceException;

    void addClient(User user, IObserver client);

    IObserver removeClient(User user);
}
