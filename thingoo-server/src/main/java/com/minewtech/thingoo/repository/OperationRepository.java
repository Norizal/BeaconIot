package com.minewtech.thingoo.repository;


import com.minewtech.thingoo.model.operation.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, String> {
    Optional<Operation> findOneByUuid(String uuid);
    Page<Operation> findByUserId(String userId, Pageable pageable);
    Page<Operation> findAll(Pageable pageable);
    @Query("select  o from Operation o where o.gateway_mac=:gatewayMac")
    Operation findByGateway_mac(@Param("gatewayMac") String gatewayMac);
}
