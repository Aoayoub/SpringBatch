package com.example.springbatch.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person")
public class PersonEntity {
    @Id
    @GeneratedValue
    private Long identifier;
    @Column(name = "username")
    private String username;
    @Column(name = "firstname")
    private String firstName;
    @Column(name="lastname")
    private String lastName;

}
