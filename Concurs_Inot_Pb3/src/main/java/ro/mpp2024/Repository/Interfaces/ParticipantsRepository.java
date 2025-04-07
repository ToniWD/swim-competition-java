package ro.mpp2024.Repository.Interfaces;

import ro.mpp2024.Domain.Participant;

public interface ParticipantsRepository extends Repository<Long, Participant>{

    Iterable<Participant> getParticipantsByEvent(Long eventId);

    Iterable<Participant> getParticipantsByEvent(Long eventId, String fullName);
}
