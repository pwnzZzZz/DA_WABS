CREATE TABLE desk (
    id SERIAL PRIMARY KEY,
    desk_nr VARCHAR(255) NOT NULL,
    nr_of_monitors INTEGER CHECK (nr_of_monitors >= 0 AND nr_of_monitors <= 10),
    booking_status VARCHAR(255) NOT NULL DEFAULT 'NONE'
);

CREATE TABLE port (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    desk_id INTEGER,
    FOREIGN KEY (desk_id) REFERENCES desk(id)
);

CREATE TABLE authorities (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE employee (
    id SERIAL PRIMARY KEY,
    fname VARCHAR(50),
    lname VARCHAR(50),
    nick VARCHAR(10),
    email VARCHAR(255),
    password VARCHAR(50),
    role VARCHAR(255)
);

CREATE TABLE publicHoliday (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL CHECK (date >= CURRENT_DATE),
    description VARCHAR(255) NOT NULL,
    is_booking_allowed BOOLEAN NOT NULL
); 

CREATE TABLE time_slot (
	id SERIAL PRIMARY KEY,
	start_time TIME,
	end_time TIME,
	name VARCHAR(255)
);

CREATE TABLE desk_booking (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL,
    desk_id INTEGER NOT NULL,
    date DATE NOT NULL CHECK (date >= CURRENT_DATE),
    time_slot_id INTEGER,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    start_time TIME,
    end_time TIME,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (desk_id) REFERENCES desk(id),
    FOREIGN KEY (time_slot_id) REFERENCES time_slot(id)
);

CREATE TABLE error_details(
	id SERIAL PRIMARY KEY,
	title VARCHAR(255),
	message VARCHAR(255)
);

