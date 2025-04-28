package Network.DTO;

import Models.Entity;
import Models.SwimmingEvent;

import java.util.Objects;

public class DTOSwimmingEvent extends Entity<Long> {
    private int distance;
    private String style;
    private int nrParticipants = -1;

    public DTOSwimmingEvent(int distance, String style) {
        this.distance = distance;
        this.style = style;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getNrParticipants() {
        return nrParticipants;
    }

    public void setNrParticipants(int nrParticipants) {
        this.nrParticipants = nrParticipants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOSwimmingEvent event = (DTOSwimmingEvent) o;
        return distance == event.distance && Objects.equals(style, event.style) && Objects.equals(this.getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(),distance, style);
    }
}
