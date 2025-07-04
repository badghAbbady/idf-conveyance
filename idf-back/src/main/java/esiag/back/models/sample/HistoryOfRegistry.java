package esiag.back.models.sample;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="historyOfRegistry")
public class HistoryOfRegistry {
    @Id
    private Long idHistory;

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
    private TransportationMean transportationMeanH;



}
