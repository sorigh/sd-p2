package com.example.monitoring_service.repositories;

import com.example.monitoring_service.entities.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, Long> {
}