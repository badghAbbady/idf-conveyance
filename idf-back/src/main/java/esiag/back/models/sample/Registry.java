package esiag.back.models.sample;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
@Table(name="registry")
public class Registry {
    @Id
    private Long idRegistry;


    @ManyToOne
    @JoinColumn(name = "subscriber")
    private Subscriber subscriber;

    @Column(name = "exitStation")
    private String exitStation;

    @Column(name = "destinationTime")
    private String destinationTime;

    @Column(name="entryStation")
    private String entryStation;

    @Column(name="boardingTime")
    private String  boardingTime;

    @Column(name = "dateRegistry")
    private String dateRegistry;


    @ManyToOne
    @JsonIgnore
    private TransportationMean transportationMean;


 public Registry() {}
}
