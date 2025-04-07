package ro.mpp2024.Service;

import ro.mpp2024.Domain.Participant;
import ro.mpp2024.Domain.SwimmingEvent;
import ro.mpp2024.Service.Interfaces.IAuthService;
import ro.mpp2024.Service.Interfaces.IMainService;
import ro.mpp2024.Service.Interfaces.IServices;

public class Services implements IServices {
    private final IAuthService authService;
    private final IMainService mainService;

    public Services(IAuthService authService, IMainService mainService) {
        this.authService = authService;
        this.mainService = mainService;
    }

    @Override
    public boolean authentificate(String username, String password) {
        return authService.authentificate(username, password);
    }

    @Override
    public Iterable<SwimmingEvent> getSwimmingEvents() {
        return mainService.getSwimmingEvents();
    }

    @Override
    public Iterable<Participant> getParticipantsByEvent(SwimmingEvent event) {
        return mainService.getParticipantsByEvent(event);
    }

    @Override
    public Iterable<Participant> getParticipantsByEvent(SwimmingEvent event, String fullName) {
        return mainService.getParticipantsByEvent(event, fullName);
    }

    @Override
    public void addParticipant(String firstName, String lastName, int age, Iterable<SwimmingEvent> swimmingEvents) {
        mainService.addParticipant(firstName, lastName, age, swimmingEvents);
    }
}
