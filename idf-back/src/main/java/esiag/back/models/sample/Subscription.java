package esiag.back.models.sample;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="subscription")
public class Subscription {

    @Id
    @Column(name="subscreptionId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long titrId;

    @Column(name="subscriptionTitle")
    private String subscriptionTitle;

    @Column(name="tariff")
    private double tariff;

    @Column(name = "subscriptionDuration")
    private String subscriptionDuration;


    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private Subscriber subscriber;
}

