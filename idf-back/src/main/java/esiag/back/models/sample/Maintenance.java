package esiag.back.models.sample;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="maintenance")
public class Maintenance {

    @Id
    @Column(name="maintenanceId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceId;

    @Column(name="maintenanceDuration")
    private String maintenanceDuration;

    @Column(name="entryDate")
    private Date Entrydate;
}
