package Network.JsonProtocol;

import Models.User;
import Network.DTO.DTOParticipant;
import Network.DTO.DTOSwimmingEvent;

import java.util.Arrays;

public class Request {
    private RequestType type;
    private User user;
    private DTOSwimmingEvent[] swimmingEvents;
    private String nameFilter;
    private DTOSwimmingEvent swimmingEvent;
    private DTOParticipant participant;

    public Request(){}
    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public DTOSwimmingEvent[] getSwimmingEvents() {
        return swimmingEvents;
    }

    public void setSwimmingEvents(DTOSwimmingEvent[] swimmingEvents) {
        this.swimmingEvents = swimmingEvents;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public DTOSwimmingEvent getSwimmingEvent() {
        return swimmingEvent;
    }

    public void setSwimmingEvent(DTOSwimmingEvent swimmingEvent) {
        this.swimmingEvent = swimmingEvent;
    }

    public DTOParticipant getParticipant() {
        return participant;
    }

    public void setParticipant(DTOParticipant participant) {
        this.participant = participant;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", user=" + user +
                ", swimmingEvents=" + Arrays.toString(swimmingEvents) +
                ", nameFilter='" + nameFilter + '\'' +
                ", swimmingEvent=" + swimmingEvent +
                ", participant=" + participant +
                '}';
    }
}
