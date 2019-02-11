package com.minewtech.thingoo.model.gateway;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minewtech.thingoo.model.status.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
public class Gateway  implements Serializable {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Getter @Setter private String uuid;
    @Getter @Setter private String userId;
    @Getter @Setter private String name;
    @Getter @Setter private String mac;
    @Enumerated(EnumType.STRING)
    @Getter @Setter private GatewayStatus status;
    @Getter @Setter private String description;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date updatedAt;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdAt;

//    @JsonIgnore @OneToMany
//    @JoinColumn(name = "gatewayMac", referencedColumnName = "mac", insertable = false, updatable = false)
//    private Collection<Status> bs = new ArrayList<Status>();

    public Gateway() {
    }
}
