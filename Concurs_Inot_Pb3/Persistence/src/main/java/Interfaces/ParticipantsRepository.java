package Interfaces;

import Models.Participant;

/**
 * Repository interface for managing {@link Participant} entities.
 */
public interface ParticipantsRepository extends Repository<Long, Participant> {

    /**
     * Returns all participants enrolled in the swimming event identified by the given ID.
     *
     * @param eventId the ID of the swimming event
     * @return an {@code Iterable} of {@link Participant}s enrolled in the event
     */
    Iterable<Participant> getParticipantsByEvent(Long eventId);

    /**
     * Returns all participants enrolled in the specified swimming event and filtered by full name.
     *
     * @param eventId  the ID of the swimming event
     * @param fullName the full name of the participant to filter by
     * @return an {@code Iterable} of {@link Participant}s matching the filter
     */
    Iterable<Participant> getParticipantsByEvent(Long eventId, String fullName);
}
