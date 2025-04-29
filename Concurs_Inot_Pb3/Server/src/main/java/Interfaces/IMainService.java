package Interfaces;



import Models.Participant;
import Models.SwimmingEvent;
import Models.User;
import Utils.ServiceException;

public interface IMainService {
    /**
     * Retrieves all swimming events.
     *
     * @return an {@code Iterable} of all {@link SwimmingEvent}s
     * @throws ServiceException if an error occurs while retrieving the events
     */
    Iterable<SwimmingEvent> getSwimmingEvents() throws ServiceException;

    /**
     * Retrieves all participants enrolled in a given swimming event.
     *
     * @param event the {@link SwimmingEvent} for which to retrieve participants
     * @return an {@code Iterable} of {@link Participant}s
     * @throws ServiceException if an error occurs while retrieving participants
     */
    Iterable<Participant> getParticipantsByEvent(SwimmingEvent event) throws ServiceException;

    /**
     * Retrieves participants enrolled in a given swimming event, filtered by full name.
     *
     * @param event     the {@link SwimmingEvent} for which to retrieve participants
     * @param fullName  the full name of the participant to filter by
     * @return an {@code Iterable} of {@link Participant}s
     * @throws ServiceException if an error occurs while retrieving participants
     */
    Iterable<Participant> getParticipantsByEvent(SwimmingEvent event, String fullName) throws ServiceException;

    /**
     * Adds a new participant and enrolls them in the given swimming events.
     *
     * @param firstName      the first name of the participant
     * @param lastName       the last name of the participant
     * @param age            the age of the participant
     * @param swimmingEvents the events in which the participant will be enrolled
     * @throws ServiceException if the participant data is invalid or enrollment fails
     */
    void addParticipant(String firstName, String lastName, int age, Iterable<SwimmingEvent> swimmingEvents) throws ServiceException;


    /**
     * Add a client to a specific user
     *
     * @param user the {@link User}
     * @param client the {@link IObserver} client to be notified
     */
    void addClient(User user, IObserver client);

    /**
     * Remove the user client
     *
     * @param user the {@link User}
     */
    IObserver removeClient(User user);
}
