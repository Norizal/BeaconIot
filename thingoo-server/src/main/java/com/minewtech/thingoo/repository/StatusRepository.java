package com.minewtech.thingoo.repository;

import com.minewtech.thingoo.model.device.DeviceType;
import com.minewtech.thingoo.model.operation.Operation;
import com.minewtech.thingoo.model.status.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface StatusRepository extends JpaRepository<Status, String> {
    Optional<Status> findOneByUuid(String uuid);
//    Page<Status> findByUserId(String userId, Pageable pageable);
    Page<Status> findAll(Pageable pageable);
    Page<Status> findAll(Specification<Status> spec, Pageable pageable);
    List<Status> findAllByGatewayMacAndMacIn(String gatewayMac,String macs);
    List<Status> findAllByGatewayMacAndMacIn(String gatewayMac, Collection<String> macs);
    List<Status> findAllByGatewayMacAndMacInAndTypeIn(String gatewayMac, Collection<String> macs, Collection<DeviceType> types);
}
