package com.itkolleg.bookingsystem.domains;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
public class CustomEmployeeDetails extends User {

    private Long id;

    public CustomEmployeeDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id) {
        super(username, password, authorities);
        this.id = id;
    }

    public CustomEmployeeDetails(Employee employee) {
        super(employee.getNick(), employee.getPassword(), new ArrayList<>());
        this.id = employee.getId();
    }

    public Long getId() {
        return id;
    }
}
