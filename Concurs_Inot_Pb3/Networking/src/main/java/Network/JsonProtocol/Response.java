package Network.JsonProtocol;

import Network.DTO.DTOParticipant;
import Network.DTO.DTOSwimmingEvent;

import java.io.Serializable;
import java.util.Arrays;

public class Response implements Serializable {
    private ResponseType type;
    private String errorMessage;
    private DTOSwimmingEvent[] swimmingEvents;
    private DTOParticipant[] participants;

    public Response() {
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public DTOSwimmingEvent[] getSwimmingEvents() {
        return swimmingEvents;
    }

    public void setSwimmingEvents(DTOSwimmingEvent[] swimmingEvents) {
        this.swimmingEvents = swimmingEvents;
    }

    public DTOParticipant[] getParticipants() {
        return participants;
    }

    public void setParticipants(DTOParticipant[] participants) {
        this.participants = participants;
    }


    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", errorMessage='" + errorMessage + '\'' +
                ", swimmingEvents=" + Arrays.toString(swimmingEvents) +
                ", participants=" + Arrays.toString(participants) +
                '}';
    }
}

