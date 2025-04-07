package ro.mpp2024.Service.Interfaces;

import ro.mpp2024.Domain.Participant;
import ro.mpp2024.Domain.SwimmingEvent;

import java.util.Iterator;
import java.util.Properties;

public interface IMainService {

    Iterable<SwimmingEvent> getSwimmingEvents();

    Iterable<Participant> getParticipantsByEvent(SwimmingEvent event);

    Iterable<Participant> getParticipantsByEvent(SwimmingEvent event, String fullName);

    void addParticipant(String firstName, String lastName, int age, Iterable<SwimmingEvent> swimmingEvents);
}
