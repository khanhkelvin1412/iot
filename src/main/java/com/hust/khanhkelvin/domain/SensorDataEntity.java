package com.hust.khanhkelvin.domain;

import com.hust.khanhkelvin.utils.SensorType;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sensor_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@ToString
public class SensorDataEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sensor_type")
    private SensorType sensorType;

    private boolean status;

    private Integer temp;

    private Integer hum;

    @Column(name = "gas_concentration")
    private Double gasConcentration;

    @Column(name = "house_sensor_id")
    private Long houseSensorId;

}
