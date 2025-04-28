package Interfaces;
import Models.Participant;

public interface ParticipantsRepository extends Repository<Long, Participant>{

    Iterable<Participant> getParticipantsByEvent(Long eventId);

    Iterable<Participant> getParticipantsByEvent(Long eventId, String fullName);
}
