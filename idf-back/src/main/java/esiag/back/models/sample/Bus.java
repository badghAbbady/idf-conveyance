package esiag.back.models.sample;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "bus")
public class Bus extends TransportationMean{
}
