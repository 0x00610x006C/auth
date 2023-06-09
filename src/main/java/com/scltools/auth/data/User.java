package com.scltools.auth.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Table(value = "users")
public class User
{
    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    public static User of(String firstName, String lastName, String email, String password)
    {
        return new User(null, firstName, lastName, email, password);
    }

    @PersistenceConstructor
    private User(Long id, String firstName, String lastName, String email, String password)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
