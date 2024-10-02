package com.itkolleg.bookingsystem.controller.room;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.service.room.RoomService;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomDeletionNotPossibleException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.concurrent.ExecutionException;



/**
 * The RoomWebController class handles HTTP requests related to rooms in the booking system.
 * It provides methods for adding, updating, deleting, and retrieving rooms.
 * The class is annotated with @Controller to indicate that it is a Spring MVC controller.
 * The base request mapping for the class is "/web/rooms".
 *
 * @author Patrick Bayr
 */


@Controller
@RequestMapping("/web/rooms")
public class RoomWebController {

    RoomService roomService;

    /**
     * Constructs a new RoomWebController with the given RoomService.
     *
     * @param roomService the RoomService to be used
     */
    public RoomWebController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Handles the GET request for "/allRooms" endpoint.
     * Retrieves all rooms from the RoomService and returns a ModelAndView with the rooms.
     *
     * @return a ModelAndView containing the "room/allRooms" view and the list of rooms
     * @throws ExecutionException   if an execution exception occurs
     * @throws InterruptedException if the thread is interrupted
     */
    @GetMapping("/allRooms")
    public ModelAndView allrooms() throws ExecutionException, InterruptedException {
        List<Room> allRooms = roomService.getAllRooms();
        return new ModelAndView("room/allRooms", "rooms", allRooms);
    }

    /**
     * Handles the GET request for "/allRoomsEmployee" endpoint.
     * Retrieves all rooms from the RoomService and returns a ModelAndView with the rooms for employees.
     *
     * @return a ModelAndView containing the "room/allRoomsEmployee" view and the list of rooms for employees
     * @throws ExecutionException   if an execution exception occurs
     * @throws InterruptedException if the thread is interrupted
     */
    @GetMapping("/allRoomsEmployee")
    public ModelAndView allroomsEmployee() throws ExecutionException, InterruptedException {
        List<Room> allRooms = roomService.getAllRooms();
        return new ModelAndView("room/allRoomsEmployee", "roomsEmployee", allRooms);
    }

    /**
     * Handles the GET request for "/addRoom" endpoint.
     * Initializes a new room object and adds it to the model.
     *
     * @param model the model to be used
     * @return a ModelAndView containing the "room/addRoom" view and the room model
     */
    @GetMapping("/addRoom")
    public ModelAndView addRoom(Model model) {
        model.addAttribute("newRoom", new Room());
        return new ModelAndView("room/addRoom", "Room", model);
    }

    /**
     * Handles the POST request for "/addRoom" endpoint.
     * Adds a new room to the RoomService if there are no binding errors,
     * otherwise returns the "/room/addRoom" view.
     *
     * @param room           the room object to be added
     * @param bindingResult  the binding result object
     * @return a redirect URL to "/web/rooms/allRooms" on success, or the "/room/addRoom" view on errors
     * @throws ExecutionException   if an execution exception occurs
     * @throws InterruptedException if the thread is interrupted
     */
    @PostMapping("/addRoom")
    public String addRoom(@Valid Room room, BindingResult bindingResult) throws ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return "/room/addRoom";
        } else {
            this.roomService.addRoom(room);
            return "redirect:/web/rooms/allRooms";
        }
    }

    /**
     * Handles the GET request for "/updateRoom/{id}" endpoint.
     * Retrieves the room with the specified ID from the RoomService and adds it to the model.
     *
     * @param id    the ID of the room to be updated
     * @param model the model to be used
     * @return a ModelAndView containing the "room/editRoom" view and the room model
     * @throws RoomNotFoundException if the room is not found
     * @throws ExecutionException     if an execution exception occurs
     * @throws InterruptedException   if the thread is interrupted
     */
    @GetMapping("/updateRoom/{id}")
    public ModelAndView updateRoom(@PathVariable Long id, Model model) throws RoomNotFoundException, ExecutionException, InterruptedException {
        Room room = this.roomService.getRoomById(id);
        model.addAttribute("updateRoom", room);
        return new ModelAndView("room/editRoom", "room", model);
    }

    /**
     * Handles the POST request for "/updateRoom" endpoint.
     * Updates the room in the RoomService if there are no binding errors,
     * otherwise returns the "/room/editRoom" view.
     *
     * @param room           the updated room object
     * @param bindingResult  the binding result object
     * @return a redirect URL to "/web/rooms/allRooms" on success, or the "/room/editRoom" view on errors
     * @throws ExecutionException         if an execution exception occurs
     * @throws InterruptedException       if the thread is interrupted
     * @throws RoomNotFoundException     if the room is not found
     */
    @PostMapping("/updateRoom")
    public String updateroom(@Valid Room room, BindingResult bindingResult) throws ExecutionException, InterruptedException, RoomNotFoundException {
        if (bindingResult.hasErrors()) {
            return "/room/editRoom";
        } else {
            this.roomService.updateRoom(room);
            return "redirect:/web/rooms/allRooms";
        }
    }

    /**
     * Handles the GET request for "/deleteRoom/{id}" endpoint.
     * Deletes the room with the specified ID from the RoomService.
     *
     * @param id the ID of the room to be deleted
     * @return a redirect URL to "/web/rooms/allRooms" on success or if deletion is not possible,
     *         or the "/web/rooms/allRooms" view if an exception occurs
     */
    @GetMapping("/deleteRoom/{id}")
    public String deleteroom(@PathVariable Long id) {
        try {
            this.roomService.deleteRoomById(id);
            return "redirect:/web/rooms/allRooms";
        } catch (RoomDeletionNotPossibleException e) {
            return "redirect:/web/rooms/allRooms";
        }
    }

    /**
     * Handles the GET request for "/floors" endpoint.
     * Retrieves all rooms from the RoomService and returns a ModelAndView with the floors.
     *
     * @return a ModelAndView containing the "room/floors" view and the list of floors
     * @throws ExecutionException   if an execution exception occurs
     * @throws InterruptedException if the thread is interrupted
     */
    @GetMapping("/floors")
    public ModelAndView allfloors() throws ExecutionException, InterruptedException {
        List<Room> allFloors = roomService.getAllRooms();
        return new ModelAndView("room/floors", "floors", allFloors);
    }

    /**
     * Handles the GET request for "/floorsEmployee" endpoint.
     * Retrieves all rooms from the RoomService and returns a ModelAndView with the floors for employees.
     *
     * @return a ModelAndView containing the "room/floorsEmployee" view and the list of floors for employees
     * @throws ExecutionException   if an execution exception occurs
     * @throws InterruptedException if the thread is interrupted
     */
    @GetMapping("/floorsEmployee")
    public ModelAndView allfloorsemployee() throws ExecutionException, InterruptedException {
        List<Room> allFloors = roomService.getAllRooms();
        return new ModelAndView("room/floorsEmployee", "floors", allFloors);
    }
}
