package esiag.back.models.sample;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;


import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="station")
public class Station {

    @Id
    @Column(name="stationId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationId;

    @Column(name="latitude")
    private double latitude;

    @Column(name="longitude")
    private double longitude;

    @Column(name="stationName")
    private String stationName;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "route_stations",
            joinColumns = @JoinColumn(name = "station_id"),
            inverseJoinColumns = @JoinColumn(name = "route_id"))
    private List<Route> routesStation;

    @JsonIgnore
    @ManyToMany(mappedBy = "stations")
    private List<Schedule> schedules;
}
