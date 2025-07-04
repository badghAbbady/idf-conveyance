package esiag.back.models.sample;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="schedule")
public class Schedule {

    @Id
    @Column(name="scheduleId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;


    @ElementCollection
    private List<Integer> durations;

    @Column(name="departuleHour")
    private String departureHour;

    @Column(name="arrivalHour")
    private String arrivalHour;


    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "scheduleStations",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "station_id"))
    private List<Station> stations;


    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "transportationMeanId")
    private TransportationMean transportationMean;


}
