package ro.mpp2024;

import com.sun.tools.javac.Main;
import ro.mpp2024.Domain.Participant;
import ro.mpp2024.Domain.Record;
import ro.mpp2024.Domain.SwimmingEvent;
import ro.mpp2024.Domain.User;
import ro.mpp2024.Repository.DBRepositories.ParticipantsDBRepo;
import ro.mpp2024.Repository.DBRepositories.RecordsDBRepo;
import ro.mpp2024.Repository.DBRepositories.SwimmingEventsDBRepo;
import ro.mpp2024.Repository.DBRepositories.UsersDBRepo;
import ro.mpp2024.Repository.Interfaces.ParticipantsRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Repository.Interfaces.RecordsRepository;
import ro.mpp2024.Repository.Interfaces.SwimmingEventsRepository;
import ro.mpp2024.Repository.Interfaces.UsersRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class App {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Acesta este un mesaj de test pentru log!");
        logger.error("Eroare de test!");

        Properties prop = new Properties();
        try{
            prop.load(new FileReader("db.config"));
        }
        catch(IOException e){
            System.out.println("Cannot find bd.config " + e);
        }

        ParticipantsRepository repoParticipants = new ParticipantsDBRepo(prop);
        SwimmingEventsRepository repoSwim = new SwimmingEventsDBRepo(prop);
        RecordsRepository repoRecords = new RecordsDBRepo(prop);
        UsersRepository repoUsers = new UsersDBRepo(prop);

        Participant p = new Participant("test","test last name", 9);
        SwimmingEvent s = new SwimmingEvent(300, "test style");
        User u = new User("test user name","test password");

        Record r = new Record(p,s);

        Iterable<Participant> it = repoParticipants.findAll();
        it.forEach(x -> {
            System.out.println(x.getFirstName() + " " + x.getLastName());
        });

        Iterable<SwimmingEvent> it2 = repoSwim.findAll();
        it2.forEach(x -> {
            System.out.println(x.getStyle());
        });

        Iterable<Record> it3 = repoRecords.findAll();
        it3.forEach(x -> {
            System.out.println(x.getParticipant().getId() + " " + x.getSwimmingEvent().getId());
        });

        Iterable<User> it4 = repoUsers.findAll();
        it4.forEach(x -> {
            System.out.println(x.getUsername() + " " + x.getPassword());
        });

    }
}
