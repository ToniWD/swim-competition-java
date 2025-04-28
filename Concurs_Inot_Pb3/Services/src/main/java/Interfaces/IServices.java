package Interfaces;


import Models.Participant;
import Models.SwimmingEvent;
import Models.User;
import Utils.ServiceException;

public interface IServices {

    Iterable<SwimmingEvent> getSwimmingEvents() throws ServiceException;

    Iterable<Participant> getParticipantsByEvent(SwimmingEvent event) throws ServiceException;

    Iterable<Participant> getParticipantsByEvent(SwimmingEvent event, String fullName) throws ServiceException;

    void addParticipant(String firstName, String lastName, int age, Iterable<SwimmingEvent> swimmingEvents) throws ServiceException;

    boolean login(String username, String password, IObserver client);

    void logout(User user, IObserver client) throws ServiceException;

    void setClient(IObserver client);

}
