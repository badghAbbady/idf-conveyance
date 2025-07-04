package esiag.back.models.sample;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="subscriber")
public class Subscriber extends Client{

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "phoneNumber")
    private double phoneNumber;



    @OneToMany(mappedBy = "subscriber")
    @JsonIgnore
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "subscriber")
    @JsonIgnore
    private List<Registry> registries;

    @OneToMany(mappedBy = "subscriber")
    @JsonIgnore
    private List<HistoryOfRegistry> history;




}
