package esiag.back.models.sample;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="transportationmean")
@Inheritance(strategy = InheritanceType.JOINED)
public class TransportationMean {

    @Id
    @Column(name="transportationMeanId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transportationMeanId;

    @Column(name="ligneCode")
    private String ligneCode;

    @Column(name="state")
    private String state;



    @JsonIgnore
    @ManyToOne
    private Route route;


    @JsonIgnore
    @OneToOne(mappedBy = "transportationMean")
    private Schedule schedule;

    @JsonIgnore
    @OneToMany(mappedBy = "transportationMean")
    private List<Registry> registries = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "transportationMeanH")
    private List<HistoryOfRegistry> registriesH = new ArrayList<>();
}
