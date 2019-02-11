package com.minewtech.thingoo.repository;


import com.minewtech.thingoo.model.gateway.Gateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GatewayRepository extends JpaRepository<Gateway, String> {
    Optional<Gateway> findOneByUuid(String uuid);
    Page<Gateway> findByUserId(String userId, Pageable pageable);
    List<Gateway> findAllByUserId(String userId);
    List<Gateway> findAllByUserIdAndMacIn(String userId, Collection<String> macs);
    Page<Gateway> findAll(Pageable pageable);
    Optional<Gateway> findOneByMac(String mac);
    List<Gateway> findByMac(String mac);
}
