package com.minewtech.thingoo.repository;


import com.minewtech.thingoo.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findAll(Pageable pageable);
    Page<User> findByUserId(String userId, Pageable pageable);
    Page<User> findAll(Specification<User> spec, Pageable pageable);

    Optional<User> findOneByUuid(String uuid);
    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByUuidAndPassword(String uuid, String password);
    Optional<User> findOneByEmailAndPassword(String email, String password);

}

