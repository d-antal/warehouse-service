package com.vodafone.exercise.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vodafone.exercise.warehouse.model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

	@Query(value = "SELECT * FROM Device d WHERE d.status='ACTIVE' ORDER BY d.pin ASC", nativeQuery = true)
	List<Device> findActiveDevicesOrderedByPinAsc();
}
