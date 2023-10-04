package com.zerobase.tablereservation.visitor.repository;

import com.zerobase.tablereservation.visitor.domain.ReserveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<ReserveEntity, Long> {
}
