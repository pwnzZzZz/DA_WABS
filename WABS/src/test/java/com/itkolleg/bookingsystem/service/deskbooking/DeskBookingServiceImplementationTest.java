package com.itkolleg.bookingsystem.service.deskbooking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.itkolleg.bookingsystem.domains.booking.DeskBooking;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.repos.deskbooking.DeskBookingRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
public class DeskBookingServiceImplementationTest {

    @Mock
    DeskBookingRepo deskBookingRepo;

    @InjectMocks
    DeskBookingServiceImplementation deskBookingService;

    @Test
    public void testAddDeskBooking() throws Exception {
        DeskBooking mockBooking = new DeskBooking();
        mockBooking.setDate(LocalDate.now().plusDays(1));
        mockBooking.setStart(LocalTime.of(9, 0));
        mockBooking.setEndTime(LocalTime.of(17, 0));

        when(deskBookingRepo.getBookingsByDeskAndDateAndBookingTimeRange(anyLong(),
                any(LocalDate.class), any(LocalTime.class), any(LocalTime.class)))
                .thenReturn(Collections.emptyList());

        when(deskBookingRepo.addBooking(any(DeskBooking.class)))
                .thenReturn(mockBooking);

        DeskBooking result = deskBookingRepo.addBooking(mockBooking);

        assertNotNull(result);
        assertEquals(mockBooking.getDate(), result.getDate());
    }

    @Test
    public void testGetAllBookings() throws Exception {
        List<DeskBooking> mockBookings = new ArrayList<>();
        mockBookings.add(new DeskBooking());
        mockBookings.add(new DeskBooking());

        when(deskBookingRepo.getAllBookings()).thenReturn(mockBookings);

        List<DeskBooking> result = deskBookingRepo
                .getAllBookings();

        assertNotNull(result);
        assertEquals(2, result.size());
    }


    @Test
    public void testGetBookingById() throws ResourceNotFoundException {
        Long testId = 5L;
        DeskBooking testBooking = new DeskBooking();
        testBooking.setId(testId);

        when(deskBookingRepo.getBookingByBookingId(testId)).thenReturn(Optional.of(testBooking));

        Optional<DeskBooking> foundBooking = deskBookingRepo.getBookingByBookingId(testId);

        assertNotNull(foundBooking, "Expected non-null booking");
        assertEquals(testId, foundBooking.get().getId(), "booking id did not match expected id");
    }

    @Test
    public void testUpdateBookingById() throws Exception {
        DeskBooking mockBooking = new DeskBooking();
        mockBooking.setId(1L);
        mockBooking.setDate(LocalDate.now().plusDays(1));
        mockBooking.setStart(LocalTime.of(9, 0));
        mockBooking.setEndTime(LocalTime.of(17, 0));

        when(deskBookingRepo.getBookingByBookingId(anyLong()))
                .thenReturn(Optional.of(mockBooking));

        when(deskBookingRepo.updateBooking(any(DeskBooking.class)))
                .thenReturn(mockBooking);

        DeskBooking updatedBooking = new DeskBooking();
        updatedBooking.setId(1L);
        updatedBooking.setDate(LocalDate.now().plusDays(2));
        updatedBooking.setStart(LocalTime.of(10, 0));
        updatedBooking.setEndTime(LocalTime.of(18, 0));

        DeskBooking result = deskBookingService.updateBookingById(1L, updatedBooking);

        assertNotNull(result);
        assertEquals(updatedBooking.getDate(), result.getDate());
        assertEquals(updatedBooking.getStart(), result.getStart());
    }


    @Test
    void onPrePersist() {
    }

    @Test
    void onPostPersist() {
    }

    @Test
    void onPreUpdate() {
    }

    @Test
    void onPostUpdate() {
    }

    @Test
    void getDesk() {
    }

    @Test
    void setDesk() {
    }

    @Test
    void testToString() {
    }
}