package com.itkolleg.bookingsystem.domains.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private static final Logger logger = LoggerFactory.getLogger(BookingResponse.class);

    private boolean success;
    private String errorMessage;

}





