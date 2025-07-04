package esiag.back.models.sample;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="route")
public class Route {

    @Id
    @Column(name="routeId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    @Column(name="departureStation")
    private String departureStation;

    @Column(name="arrivalStation")
    private String arrivalStation;

    @Column(name="ligneCode")
    private String lignecode;

    @JsonIgnore
    @OneToMany(mappedBy = "route")
    private List<TransportationMean> transportationMeans;


    @ManyToMany
    @JoinTable(name = "route_stations",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "station_id"))
    @JsonIgnore
    private List<Station> stations;
}
