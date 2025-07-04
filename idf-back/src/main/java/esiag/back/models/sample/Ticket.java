package esiag.back.models.sample;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.Table;
import java.lang.reflect.GenericDeclaration;

@Entity
@Data
@Table(name="ticket")
public class Ticket {

    @Id
    @Column(name="ticketId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @Column(name = "purchaseHour")
    private String purchaseHour;

    @Column(name="serialNumber")
    private String serialNumber;

    @Column(name="tariff")
    private final double tarif=1.90;


    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;
}
