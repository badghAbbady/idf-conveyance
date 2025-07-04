package esiag.back.models.sample;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name="visitor")
@Inheritance(strategy = InheritanceType.JOINED)
public class Visitor {


    @Id
    @Column(name="visitorId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitorId;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;



}
