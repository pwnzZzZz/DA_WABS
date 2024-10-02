package com.itkolleg.bookingsystem;

import com.itkolleg.bookingsystem.domains.booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.*;
import com.itkolleg.bookingsystem.domains.booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.booking.RoomBooking;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotAvailableException;
import com.itkolleg.bookingsystem.repos.desk.DeskRepo;
import com.itkolleg.bookingsystem.repos.deskbooking.DeskBookingRepo;
import com.itkolleg.bookingsystem.repos.employee.EmployeeDBAccess;
import com.itkolleg.bookingsystem.repos.holiday.HolidayRepo;
import com.itkolleg.bookingsystem.repos.ressource.DBAccessRessource;
import com.itkolleg.bookingsystem.repos.ressourcebooking.RessourceBookingRepo;
import com.itkolleg.bookingsystem.repos.room.DBAccessRoom;
import com.itkolleg.bookingsystem.repos.roombooking.RoomBookingRepo;
import com.itkolleg.bookingsystem.repos.timeslot.TimeslotRepo;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.itkolleg.bookingsystem.*")
//@ComponentScan("com.itkolleg.Bookingsystem")//to scan repository files
@EntityScan("com.itkolleg.bookingsystem.*")
public class WABS implements ApplicationRunner {

    //

    final EmployeeDBAccess employeeDBAccess;
    final DeskRepo deskRepo;
    final DeskBookingRepo deskBookingRepo;
    final TimeslotRepo timeSlotRepo;
    final HolidayRepo holidayRepo;
    final DBAccessRessource dbAccessRessource;
    final RessourceBookingRepo ressourceBookingRepo;
    final DBAccessRoom dbAccessRoom;
    final RoomBookingRepo roombookingRepo;

    public WABS(EmployeeDBAccess employeeDBAccess, DeskRepo deskRepo, DeskBookingRepo deskBookingRepo, TimeslotRepo timeSlotRepo, HolidayRepo holidayRepo, DBAccessRessource dbAccessRessource, RessourceBookingRepo ressourceBookingRepo, DBAccessRoom dbAccessRoom, RoomBookingRepo roombookingRepo) {
        this.employeeDBAccess = employeeDBAccess;
        this.deskRepo = deskRepo;
        this.deskBookingRepo = deskBookingRepo;
        this.timeSlotRepo = timeSlotRepo;
        this.holidayRepo = holidayRepo;
        this.dbAccessRessource = dbAccessRessource;
        this.ressourceBookingRepo = ressourceBookingRepo;
        this.dbAccessRoom = dbAccessRoom;
        this.roombookingRepo = roombookingRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(WABS.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("\n\nSystem is up and running!\n");


        Room room1 = this.dbAccessRoom.addRoom(new Room(1L, "1"));
        Room room2 = this.dbAccessRoom.addRoom(new Room(2L, "1"));
        Room room3 = this.dbAccessRoom.addRoom(new Room(3L, "1"));
        Room room4 = this.dbAccessRoom.addRoom(new Room(4L, "1"));
        Room room5 = this.dbAccessRoom.addRoom(new Room(5L, "1"));
        Room room6 = this.dbAccessRoom.addRoom(new Room(6L, "1"));


        Ressource test1 = this.dbAccessRessource.addRessource(new Ressource(1L, Ressourcetype.BEAMER, "Test1", "TestBeamer", "InfoBeamer", "BeamerSN"));
        Ressource test2 = this.dbAccessRessource.addRessource(new Ressource(2L, Ressourcetype.BEAMER, "Test2", "TestBeamer", "InfoBeamer", "BeamerSN"));
        Ressource test3 = this.dbAccessRessource.addRessource(new Ressource(3L, Ressourcetype.BEAMER, "Test3", "TestBeamer", "InfoBeamer", "BeamerSN"));
        Ressource test4 = this.dbAccessRessource.addRessource(new Ressource(4L, Ressourcetype.BEAMER, "Test4", "TestBeamer", "InfoBeamer", "BeamerSN"));


        Employee operator = this.employeeDBAccess.saveEmployee(new Employee("Patrick", "Bayr", "operator", "bayr2@hotmail.com", "password", Role.ROLE_OPERATOR));
        Employee pemployee = this.employeeDBAccess.saveEmployee(new Employee("Manuel", "Payr", "nemployee", "bayr@hotmail.com", "password", Role.ROLE_N_EMPLOYEE));
        Employee nemployee = this.employeeDBAccess.saveEmployee(new Employee("Sonja", "Lechner", "pemployee", "bayr1@hotmail.com", "password", Role.ROLE_P_EMPLOYEE));

        Employee admin = this.employeeDBAccess.saveEmployee(new Employee("Marcel", "Schranz", "admin", "marcel-schranz@hotmail.com", "password", Role.ROLE_ADMIN));
        Employee sonlech = this.employeeDBAccess.saveEmployee(new Employee("Sonja", "Lechner", "sonlech", "sonja52@gmail.com", "password", Role.ROLE_ADMIN));
        Employee jaslech = this.employeeDBAccess.saveEmployee(new Employee("Jason", "Lechner", "jaslech", "jason_lechner@gmail.com", "password", Role.ROLE_P_EMPLOYEE));
        Employee joslech = this.employeeDBAccess.saveEmployee(new Employee("Josan", "Lechner", "joslech", "josan_strobie@gmail.com", "password", Role.ROLE_N_EMPLOYEE));
        Employee camlech = this.employeeDBAccess.saveEmployee(new Employee("Camil", "Lechner", "camlech", "camil@gmail.com", "password", Role.ROLE_OPERATOR));


        LocalDate rdate = LocalDate.now();
        LocalTime rtime1 = LocalTime.of(8, 0, 0, 0);
        LocalTime rtime2 = LocalTime.of(10, 0, 0, 0);
        RessourceBooking rBooking1 = this.ressourceBookingRepo.addBooking(new RessourceBooking(admin, test1, rdate, rtime1, rtime2));
        try {
            RoomBooking roomBooking1 = this.roombookingRepo.addBooking(new RoomBooking(admin, room1, rdate, rtime1, rtime2));
        } catch (RoomNotAvailableException e) {
            throw new RuntimeException(e);
        }
        try {
            RoomBooking roomBooking2 = this.roombookingRepo.addBooking((new RoomBooking(admin, room2, rdate, rtime1, rtime2)));
        } catch (RoomNotAvailableException e) {
            throw new RuntimeException(e);
        }
        try {
            RoomBooking roomBooking3 = this.roombookingRepo.addBooking((new RoomBooking(admin, room3, rdate, rtime1, rtime2)));
        } catch (RoomNotAvailableException e) {
            throw new RuntimeException(e);
        }
        try {
            RoomBooking roomBooking4 = this.roombookingRepo.addBooking((new RoomBooking(jaslech, room4, rdate, rtime1, rtime2)));
        } catch (RoomNotAvailableException e) {
            throw new RuntimeException(e);
        }
        rdate = LocalDate.of(2023, 10, 30);
        rtime1 = LocalTime.of(12, 0, 0, 0);
        rtime2 = LocalTime.of(20, 0, 0, 0);
        RessourceBooking rBooking2 = this.ressourceBookingRepo.addBooking(new RessourceBooking(admin, test3, rdate, rtime1, rtime2));
        rdate = LocalDate.of(2023, 10, 25);
        rtime1 = LocalTime.of(10, 0, 0, 0);
        rtime2 = LocalTime.of(11, 0, 0, 0);
        RessourceBooking rBooking3 = this.ressourceBookingRepo.addBooking(new RessourceBooking(admin, test4, rdate, rtime1, rtime2));
        rdate = LocalDate.of(2023, 10, 20);
        rtime1 = LocalTime.of(12, 0, 0, 0);
        rtime2 = LocalTime.of(15, 0, 0, 0);
        RessourceBooking rBooking4 = this.ressourceBookingRepo.addBooking(new RessourceBooking(admin, test1, rdate, rtime1, rtime2));
        rdate = LocalDate.of(2023, 10, 20);
        rtime1 = LocalTime.of(12, 0, 0, 0);
        rtime2 = LocalTime.of(15, 0, 0, 0);
        RessourceBooking rBooking5 = this.ressourceBookingRepo.addBooking(new RessourceBooking(jaslech, test2, rdate, rtime1, rtime2));

        Port hdmi = new Port("HDMI");
        Port usba = new Port("USB-A");
        Port usbb = new Port("USB-B");
        Port usbc = new Port("USB-C");
        Port micro = new Port("MICRO USB");
        Port mini = new Port("MINI USB");
        Port dvi = new Port("DVI");
        Port rj45 = new Port("ETHERNET");
        Port display = new Port("DISPLAY");
        Port vga = new Port("VGA");
        Port audio = new Port("AUDIO ONLY");
        Port io = new Port("LINE IN/OUT");
        Port thunderbolt = new Port("THUNDERBOLT");
        Port sd = new Port(" SD CARD READER");

        System.out.println("\nPorts created");

        int portList = 0;
        List<Port> d1 = new ArrayList<>();
        d1.add(usba);
        d1.add(usbc);
        d1.add(hdmi);
        d1.add(io);
        d1.add(display);
        portList++;

        List<Port> d2 = new ArrayList<>();
        d2.add(usba);
        d2.add(usbc);
        d2.add(io);
        d2.add(display);
        d2.add(micro);
        portList++;

        List<Port> d3 = new ArrayList<>();
        d3.add(rj45);
        d3.add(usbb);
        d3.add(io);
        d3.add(display);
        d3.add(audio);
        d3.add(mini);
        portList++;

        List<Port> d4 = new ArrayList<>();
        d4.add(sd);
        d4.add(io);
        d4.add(dvi);
        d4.add(vga);
        d4.add(thunderbolt);
        portList++;

        System.out.println("\n" + portList + " Lists of Ports created\n");

        int noOfDesks = 0;
        Desk desk1 = new Desk("D1-1", 2, d3);
        deskRepo.addDesk(desk1);
        noOfDesks++;
        Desk desk2 = new Desk("D1-2", 2, d2);
        deskRepo.addDesk(desk2);
        noOfDesks++;
        Desk desk3 = new Desk("D2-3", 2, d1);
        deskRepo.addDesk(desk3);
        noOfDesks++;
        Desk desk4 = new Desk("D1-3", 2, d1);
        deskRepo.addDesk(desk4);
        noOfDesks++;
        Desk desk5 = new Desk("D1-4", 2, d1);
        deskRepo.addDesk(desk5);
        noOfDesks++;
        Desk desk6 = new Desk("D2-1", 3, d1);
        deskRepo.addDesk(desk6);
        noOfDesks++;
        Desk desk7 = new Desk("D2-4", 2, d3);
        deskRepo.addDesk(desk7);
        noOfDesks++;
        Desk desk8 = new Desk("D3-3", 2, d3);
        deskRepo.addDesk(desk8);
        noOfDesks++;
        Desk desk9 = new Desk("D3-1", 2, d4);
        deskRepo.addDesk(desk9);
        noOfDesks++;
        Desk desk10 = new Desk("D3-2", 3, d4);
        deskRepo.addDesk(desk10);
        noOfDesks++;

        if (deskRepo.getAllDesks() != null) {
            System.out.println("\ndesks successfully added to the Database\n");
        } else {
            System.out.println("Error: desks  were not added to the Database.");
        }


        int noOfDeskBookings = 0;
        List<Desk> allDesks = deskRepo.getAllDesks();


        //set the booking date (Mon -Fri)
        LocalDate date = LocalDate.now().plusDays(0);
        LocalDate date1 = LocalDate.now().plusDays(1);
        LocalDate date2 = LocalDate.now().plusDays(2);
        LocalDate date3 = LocalDate.now().plusDays(3);
        LocalDate date4 = LocalDate.now().plusDays(4);


        //creat booking times combinations (start and end)
        LocalTime[] bookingTime1 = getBookingTimes(1);
        LocalTime[] bookingTime2 = getBookingTimes(2);
        LocalTime[] bookingTime3 = getBookingTimes(3);

        Timeslot morning = new Timeslot(LocalTime.of(8, 0), LocalTime.of(12, 30), "AM");
        Timeslot afternoon = new Timeslot(LocalTime.of(12, 30), LocalTime.of(17, 0), "PM");
        Timeslot fullDay = new Timeslot(LocalTime.of(8, 0), LocalTime.of(17, 0), "ALL_DAY");


        int dbTimeslot = 0;
        for (Timeslot timeSlot : Arrays.asList(morning, afternoon, fullDay)) {
            this.timeSlotRepo.addTimeslot(timeSlot);
            dbTimeslot++;
        }
        System.out.println("\n" + dbTimeslot + " Time slots successfully added to DataBase! \n");

        //get the desks from the database
        Desk savedDesk1 = deskRepo.getDeskById(desk1.getId());
        Desk savedDesk2 = deskRepo.getDeskById(desk2.getId());
        Desk savedDesk3 = deskRepo.getDeskById(desk3.getId());
        Desk savedDesk4 = deskRepo.getDeskById(desk4.getId());
        Desk savedDesk5 = deskRepo.getDeskById(desk5.getId());
        Desk savedDesk6 = deskRepo.getDeskById(desk6.getId());
        Desk savedDesk7 = deskRepo.getDeskById(desk7.getId());
        Desk savedDesk8 = deskRepo.getDeskById(desk8.getId());
        Desk savedDesk9 = deskRepo.getDeskById(desk9.getId());
        Desk savedDesk10 = deskRepo.getDeskById(desk10.getId());

        //Create 10 new deskbookings

        DeskBooking deskBooking = new DeskBooking(admin, savedDesk3, date, morning);
        noOfDeskBookings++;
        DeskBooking deskBooking1 = new DeskBooking(sonlech, savedDesk4, date1, afternoon);
        noOfDeskBookings++;
        DeskBooking deskBooking2 = new DeskBooking(jaslech, savedDesk3, date2, fullDay);
        noOfDeskBookings++;
        DeskBooking deskBooking3 = new DeskBooking(jaslech, savedDesk4, date3, fullDay);
        noOfDeskBookings++;
        DeskBooking deskBooking4 = new DeskBooking(camlech, savedDesk2, date4, fullDay);
        noOfDeskBookings++;
        DeskBooking deskBooking5 = new DeskBooking(joslech, savedDesk1, date, fullDay);
        noOfDeskBookings++;
        DeskBooking deskBooking6 = new DeskBooking(joslech, savedDesk1, date1, fullDay);
        noOfDeskBookings++;
        DeskBooking deskBooking7 = new DeskBooking(joslech, savedDesk1, date2, morning);
        noOfDeskBookings++;
        DeskBooking deskBooking8 = new DeskBooking(sonlech, savedDesk1, date3, morning);
        noOfDeskBookings++;
        DeskBooking deskBooking9 = new DeskBooking(sonlech, savedDesk1, date4, morning);
        noOfDeskBookings++;
        System.out.println("\n" + noOfDeskBookings + " desk Bookings successfully created! \n");

        try {
            this.deskBookingRepo.addBooking(deskBooking);
            this.deskBookingRepo.addBooking(deskBooking1);
            this.deskBookingRepo.addBooking(deskBooking2);
            this.deskBookingRepo.addBooking(deskBooking3);
            this.deskBookingRepo.addBooking(deskBooking4);
            this.deskBookingRepo.addBooking(deskBooking5);
            this.deskBookingRepo.addBooking(deskBooking6);
            this.deskBookingRepo.addBooking(deskBooking7);
            this.deskBookingRepo.addBooking(deskBooking8);
            this.deskBookingRepo.addBooking(deskBooking9);
        } catch (DeskNotAvailableException e) {
            throw new RuntimeException(e);
        }

        if (deskBookingRepo.getAllBookings() != null) {
            System.out.println("\ndesk Bookings successfully added to the Database\n");
        } else {
            System.out.println("Error: desk Bookings were not added to the Database.");
        }
        //Creat Holidays
        PublicHoliday assumptionOfMary = new PublicHoliday(LocalDate.of(2023,8,15)," Assumption of Mary",false);
        PublicHoliday nationalDay = new PublicHoliday(LocalDate.of(2023, 10, 26), "National Day", true);
        PublicHoliday immaculateConception = new PublicHoliday(LocalDate.of(2023, 12, 8), "Immaculate Conception", true);
        PublicHoliday christmasDay = new PublicHoliday(LocalDate.of(2023, 12, 25), "Christmas Day", false);
        PublicHoliday boxingDay = new PublicHoliday(LocalDate.of(2023, 12, 26), "Boxing Day (St. Stephen's Day)", false);

        //Add to Database
        try {

            this.holidayRepo.addHoliday(assumptionOfMary);
            this.holidayRepo.addHoliday(nationalDay);
            this.holidayRepo.addHoliday(immaculateConception);
            this.holidayRepo.addHoliday(christmasDay);
            this.holidayRepo.addHoliday(boxingDay);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        if (holidayRepo.getAllHolidays() != null) {
            System.out.println("\nHolidays successfully added to the Database\n");
        } else {
            System.out.println("Error: Holidays were not added to the Database.");
        }

    }

    private LocalTime[] getBookingTimes(int option) {
        LocalTime[] bookingTimes = new LocalTime[2];

        switch (option) {
            case 1 -> {
                // Option 1: 8:00 to 12:00 (noon)
                bookingTimes[0] = LocalTime.of(8, 0);
                bookingTimes[1] = LocalTime.of(12, 30);
            }
            case 2 -> {
                // Option 2: 13:00 to 17:00
                bookingTimes[0] = LocalTime.of(12, 30);
                bookingTimes[1] = LocalTime.of(17, 0);
            }
            case 3 -> {
                // Option 3: 8:00 to 17:00 (full day)
                bookingTimes[0] = LocalTime.of(8, 0);
                bookingTimes[1] = LocalTime.of(17, 0);
            }
            default -> throw new IllegalArgumentException("Invalid booking option.");
        }
        return bookingTimes;
    }


}


