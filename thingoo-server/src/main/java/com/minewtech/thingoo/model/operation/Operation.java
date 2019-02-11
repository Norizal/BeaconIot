package com.minewtech.thingoo.model.operation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Operation  implements Serializable {
        @Id
        @GenericGenerator(name="idGenerator", strategy="uuid")
        @GeneratedValue(generator="idGenerator")
        @Getter @Setter private String uuid;
        @Getter @Setter private String userId;
        @Getter @Setter private String gateway_mac;
        @Getter @Setter private String operation;
        @Getter @Setter private String responseCode;
        @Getter @Setter private String responseMessage;
        @Column(insertable = false, updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @Getter @Setter private Date updatedAt;
        @Column(insertable = false, updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @Getter @Setter private Date createdAt;

        public String getUuid() {
                return uuid;
        }

        public void setUuid(String uuid) {
                this.uuid = uuid;
        }

        public String getUserId() {
                return userId;
        }

        public void setUserId(String userId) {
                this.userId = userId;
        }

        public String getGateway_mac() {
                return gateway_mac;
        }

        public void setGateway_mac(String gateway_mac) {
                this.gateway_mac = gateway_mac;
        }

        public String getOperation() {
                return operation;
        }

        public void setOperation(String operation) {
                this.operation = operation;
        }

        public String getResponseCode() {
                return responseCode;
        }

        public void setResponseCode(String responseCode) {
                this.responseCode = responseCode;
        }

        public String getResponseMessage() {
                return responseMessage;
        }

        public void setResponseMessage(String responseMessage) {
                this.responseMessage = responseMessage;
        }

        public Date getUpdatedAt() {
                return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
                this.updatedAt = updatedAt;
        }

        public Date getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
                this.createdAt = createdAt;
        }

        @Override
        public String toString() {
                return "Operation{" +
                        "uuid='" + uuid + '\'' +
                        ", userId='" + userId + '\'' +
                        ", gateway_mac='" + gateway_mac + '\'' +
                        ", operation='" + operation + '\'' +
                        ", responseCode='" + responseCode + '\'' +
                        ", responseMessage='" + responseMessage + '\'' +
                        ", updatedAt=" + updatedAt +
                        ", createdAt=" + createdAt +
                        '}';
        }
}
