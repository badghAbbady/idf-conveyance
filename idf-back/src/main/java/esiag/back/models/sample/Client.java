package esiag.back.models.sample;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="client")
@Inheritance(strategy = InheritanceType.JOINED)
public class Client extends Visitor {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "phoneNumber")
    private double phoneNumber;

}