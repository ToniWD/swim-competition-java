package Network.DTO;

import Models.Participant;
import Models.SwimmingEvent;

public class DTOUtils {
    public static SwimmingEvent getFromDTO(DTOSwimmingEvent dto){
        SwimmingEvent obj = new SwimmingEvent(dto.getDistance(), dto.getStyle());
        obj.setNrParticipants(dto.getNrParticipants());
        obj.setId(dto.getId());
        return obj;
    }

    public static DTOSwimmingEvent getDTO(SwimmingEvent obj){
        DTOSwimmingEvent dto = new DTOSwimmingEvent(obj.getDistance(), obj.getStyle());
        dto.setNrParticipants(obj.getNrParticipants());
        dto.setId(obj.getId());
        return dto;
    }

    public static SwimmingEvent[] getFromDTO(DTOSwimmingEvent[] dto){
        SwimmingEvent[] objs=new SwimmingEvent[dto.length];
        for(int i=0;i<dto.length;i++){
            objs[i]=getFromDTO(dto[i]);
        }
        return objs;
    }

    public static DTOSwimmingEvent[] getDTO(SwimmingEvent[] objs){
        DTOSwimmingEvent[] dto=new DTOSwimmingEvent[objs.length];
        for(int i=0;i<objs.length;i++){
            dto[i]=getDTO(objs[i]);
        }
        return dto;
    }




    public static Participant getFromDTO(DTOParticipant dto){
        Participant obj = new Participant(dto.getFirstName(), dto.getLastName(), dto.getAge());
        obj.setId(dto.getId());
        obj.setNrEvents(dto.getNrEvents());
        return obj;
    }

    public static DTOParticipant getDTO(Participant obj){
        DTOParticipant dto = new DTOParticipant(obj.getFirstName(), obj.getLastName(), obj.getAge());
        dto.setNrEvents(obj.getNrEvents());
        dto.setId(obj.getId());
        return dto;
    }

    public static Participant[] getFromDTO(DTOParticipant[] dto){
        Participant[] objs=new Participant[dto.length];
        for(int i=0;i<dto.length;i++){
            objs[i]=getFromDTO(dto[i]);
        }
        return objs;
    }

    public static DTOParticipant[] getDTO(Participant[] objs){
        DTOParticipant[] dto=new DTOParticipant[objs.length];
        for(int i=0;i<objs.length;i++){
            dto[i]=getDTO(objs[i]);
        }
        return dto;
    }

}
