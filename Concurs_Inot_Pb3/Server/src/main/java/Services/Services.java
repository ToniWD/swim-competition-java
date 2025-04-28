package Services;

import Interfaces.IObserver;
import Interfaces.IServices;
import Models.Participant;
import Models.SwimmingEvent;
import Interfaces.IAuthService;
import Interfaces.IMainService;
import Models.User;
import Utils.RepoException;
import Utils.ServiceException;
import Validators.ValidatorException;

public class Services implements IServices {
    private final IAuthService authService;
    private final IMainService mainService;

    public Services(IAuthService authService, IMainService mainService) {
        this.authService = authService;
        this.mainService = mainService;
    }

    @Override
    public Iterable<SwimmingEvent> getSwimmingEvents() {
        try {
            return mainService.getSwimmingEvents();
        }
        catch (RepoException ex)
        {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public Iterable<Participant> getParticipantsByEvent(SwimmingEvent event) {
        try {
            return mainService.getParticipantsByEvent(event);
        }
        catch (RepoException ex)
        {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public Iterable<Participant> getParticipantsByEvent(SwimmingEvent event, String fullName) {
        try {
            return mainService.getParticipantsByEvent(event, fullName);
        }
        catch (RepoException ex)
        {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public void addParticipant(String firstName, String lastName, int age, Iterable<SwimmingEvent> swimmingEvents) {
        try {
            mainService.addParticipant(firstName, lastName, age, swimmingEvents);
        }
        catch (RepoException | ValidatorException ex)
        {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public boolean login(String username, String password, IObserver client) {
        if(authService.authentificate(username, password)){
            User user = new User(username, password);
            mainService.addClient(user, client);
            return true;
        }
        return false;
    }

    @Override
    public void logout(User user, IObserver client) throws ServiceException {
        if (authService.authentificate(user.getUsername(), user.getPassword())) {
            IObserver localClient = mainService.removeClient(user);
            if (localClient == null)
                throw new ServiceException("User " + user.getId() + " is not logged in.");
        }
    }

    @Override
    public void setClient(IObserver client) {
        return;
    }

}
