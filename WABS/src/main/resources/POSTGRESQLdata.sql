-- Insert Ports
INSERT INTO port (name) VALUES ('HDMI'), ('USB-A'), ('USB-B'), ('USB-C'), ('MICRO USB'), ('MINI USB'), ('DVI'), ('ETHERNET'), ('DISPLAY'), ('VGA'), ('AUDIO ONLY'), ('LINE IN/OUT'), ('THUNDERBOLT'), ('SD CARD READER');

-- Insert TimeSlots
INSERT INTO time_slot (id, name, start_time, end_time) VALUES (1, 'AM', '08:00:00', '12:30:00'), (2, 'PM', '12:30:00', '17:00:00'), (3, 'ALL_DAY', '08:00:00', '17:00:00');

/*-- Insert Roles
INSERT INTO role (name) VALUES ('ADMIN'), ('OPERATOR'), ('N_EMPLOYEE'), ('P_EMPLOYEE');
*/
-- Insert Employees
DO
$do$
DECLARE
  counter INTEGER := 1;
BEGIN
  WHILE counter <= 20 LOOP
    INSERT INTO employee (fname, lname, nick, email, password, role_id) VALUES ('Employee' || counter, 'Lastname' || counter, 'Nick' || counter, 'email' || counter || '@example.com', 'password', (counter % 4) + 1);
    counter := counter + 1;
  END LOOP;
END
$do$;

-- Insert Desks
DO
$do$
DECLARE
  counter INTEGER := 1;
  room INTEGER := 1;
  desk INTEGER := 1;
BEGIN
  WHILE counter <= 20 LOOP
    INSERT INTO desk (desk_nr, nr_of_monitors) VALUES ('R' || room || 'D' || desk, (counter % 3) + 1);
    desk := desk + 1;
    IF desk > 4 THEN
      desk := 1;
      room := room + 1;
    END IF;
    counter := counter + 1;
  END LOOP;
END
$do$;

-- Insert Holidays
INSERT INTO publicHoliday (date, description, is_booking_allowed) VALUES ('2023-12-8', 'Immaculate Conception', true),('2023-12-25', 'Christmas', false), ('2023-08-15', 'Assumption of Mary', false),('2024-01-01', 'New Year', false), ('2023-10-26', 'Independence Day', false);

-- Insert DeskBookings
INSERT INTO desk_booking (employee_id, desk_id, date, timeslot_id) VALUES (1, 1, '2023-08-01', 1), (2, 2, '2023-08-02', 2), (3, 3, '2023-08-03', 3)(1, 1, '2023-08-01', 1), (2, 2, '2023-08-02', 2), (4, 4, '2023-08-03', 4);
