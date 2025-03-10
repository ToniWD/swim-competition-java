package ro.mpp2024.Domain;

public class Record extends Entity<Long>{

    private Participant participant;
    private SwimmingEvent swimmingEvent;

    public Record(Participant participant, SwimmingEvent swimmingEvent) {
        this.participant = participant;
        this.swimmingEvent = swimmingEvent;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public SwimmingEvent getSwimmingEvent() {
        return swimmingEvent;
    }

    public void setSwimmingEvent(SwimmingEvent swimmingEvent) {
        this.swimmingEvent = swimmingEvent;
    }
}
