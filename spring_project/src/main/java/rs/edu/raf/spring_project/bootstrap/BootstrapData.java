package rs.edu.raf.spring_project.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.spring_project.model.auth.MyUser;
import rs.edu.raf.spring_project.model.entities.*;
import rs.edu.raf.spring_project.repositories.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Component
public class BootstrapData implements CommandLineRunner {

    private final MyUserRepository userRepository;
    private final AirlineRepository airlineRepository;
    private final CityRepository cityRepository;
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;

    public BootstrapData(MyUserRepository userRepository, AirlineRepository airlineRepository, CityRepository cityRepository, FlightRepository flightRepository, TicketRepository ticketRepository, BookingRepository bookingRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.airlineRepository = airlineRepository;
        this.cityRepository = cityRepository;
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");
        Random random = new Random();

        String[] AIRLINES_LIST1 = {"SINGAPORE","QATAR","Cathay Pacific","Qantas"};
        String[] AIRLINES_LIST2 = {"Airways","Airlines","Air","Aviation"};
        String[] CITIES = {"Paris","London","Madrid","Amsterdam","Lisbon","Budapest","Prague","Kraljevo","Nis","Zagreb","Brno","Tokio","Seul","Peking","Moskva"};
        String DEPART[] = {"01-01-2021", "13-01-2021", "07-02-2021", "13-03-2021", "14-03-2021","04-09-2021", "23-04-2021","16-05-2021","15-07-2021","18-08-2021"};
        String RETURN[] = {"10-01-2021", "21-01-2021" , "10-02-2021", "18-03-2021", "20-03-2021","09-90-2021", "30-4-2021","28-5-2021","28-7-2021","28-8-2021"};

        List<City> cities = new ArrayList<>();
        for (int i = 0; i < CITIES.length; i++) {
            City city = new City();
            String name = CITIES[i];
            city.setName(name);
            cities.add(city);
        }
        System.out.println(cityRepository.saveAll(cities));

        List<Airline> airlines = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Airline airline = new Airline();
            String name = AIRLINES_LIST1[i] + " " + AIRLINES_LIST2[i];
            airline.setName(name);
            airlines.add(airline);
        }
        System.out.println(airlineRepository.saveAll(airlines));

        List<Flight> flights = new ArrayList<>();
        for (int i = 0; i < 6; i=i+2) {
            Flight f = new Flight();
            int i1 = random.nextInt(CITIES.length-1);
            int i2 = i1 + 1;
            City c1 = cities.get(i);
            City c2 = cities.get(i+1);
            f.setOrigin(c1);
            f.setDestination(c2);
            List<Ticket> t = new ArrayList<>();
            f.setTickets(t);
            flights.add(f);
        }


        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Ticket t = new Ticket();
            t.setCount((long) 10);
            Airline a = airlines.get(i%4);
            t.setAirline(a);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date departDate= sdf.parse(DEPART[i]);
           // Date returnDate= sdf.parse(RETURN[i]);
            t.setDepartDate(departDate);
           // t.setReturnDate(returnDate);

            if(i%2==0){
                t.setOneway(true);
            }else{
                t.setOneway(false);
                Date returnDate= sdf.parse(DEPART[i]);
                t.setReturnDate(returnDate);
            }
            int rand = random.nextInt(3);
            Flight f = flights.get(rand);
            t.setFlight(f);
            flights.get(rand).getTickets().add(t);
            tickets.add(t);
        }
        System.out.println(flightRepository.saveAll(flights));
        System.out.println(ticketRepository.saveAll(tickets));


        MyUser pera = new MyUser();
        ArrayList<Booking> bookings = new ArrayList<>();
        pera.setBookings(bookings);
        pera.setUsername("valerija");
        String password = passwordEncoder.encode("1234aa");
        pera.setPassword(password);
        pera.setType(TypeOfUser.ADMIN);

        MyUser mika = new MyUser();
        ArrayList<Booking> bookings2 = new ArrayList<>();
        mika.setBookings(bookings2);
        mika.setUsername("user");
        mika.setPassword(password);
        mika.setType(TypeOfUser.USER);

        System.out.println(password);

        userRepository.save(pera);
        userRepository.save(mika);

        System.out.println("Data loaded!");
    }
}
