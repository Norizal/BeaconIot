package com.minewtech.thingoo.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
@Data
@Entity
public class User  implements Serializable {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Getter @Setter private String uuid;
    @Getter @Setter private String userId;
    @Getter @Setter private String email;
    @JsonProperty(access = Access.WRITE_ONLY)
    @Getter @Setter private String password;
    @Getter @Setter private String name;

    @Enumerated(EnumType.STRING)
    @Getter @Setter private Role role;

    @Getter @Setter private String description;

    @JsonIgnore @Getter @Setter private boolean isActive;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date updatedAt;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdAt;
    @Getter @Setter private String mobile_phone;
    @Getter @Setter private String nationcode;

    public User(){
//        this(UUID.randomUUID().toString().replace("-", ""), "", "password", "", Role.USER, "");
        this("", "", "", Role.USER, "");
    }

    public User(String uuid, String email, String name, Role role, String description) {
        this.uuid = uuid;
        this.email = email;
        this.name = name;
        this.role = role;
        this.description = description;
        this.isActive = false;
        this.updatedAt = Calendar.getInstance().getTime();
        this.createdAt = Calendar.getInstance().getTime();
    }


    public User(String uuid,String email, String password, String name, Role role, String description, boolean isActive){
        this.uuid = uuid;
        this.userId=userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.description = description;
        this.isActive = isActive;
        this.updatedAt = Calendar.getInstance().getTime();
        this.createdAt = Calendar.getInstance().getTime();
    }

}
