package com.hust.khanhkelvin.domain;

import com.hust.khanhkelvin.utils.SensorType;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sensor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@ToString
public class Sensor extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "house_id")
    private Long houseId;

    @Column(name = "data")
    private String data;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SensorType type;
}
