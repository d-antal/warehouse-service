package com.vodafone.exercise.warehouse;

import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_ACTIVE_2;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_LIST;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.ID_NOT_EXIST;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.vodafone.exercise.warehouse.model.Device;
import com.vodafone.exercise.warehouse.repository.DeviceRepository;

@DataJpaTest
class DeviceRepositoryTest {

	@Autowired
	private DeviceRepository deviceRepository;

	@BeforeEach
	public void setup() {

	}

	@Test
	void testFindByIdWhenDeviceFound() {
		deviceRepository.save(DEVICE_ACTIVE_2);
		Device device = deviceRepository.findById(DEVICE_ACTIVE_2.getId()).get();
		assertNotNull(device);
	}

	@Test
	void testFindByIdWhenDeviceNotFound() {
		assertTrue(deviceRepository.findById(ID_NOT_EXIST).isEmpty());
	}

	@Test
	void testFindAllActiveDevices() {
		deviceRepository.saveAll(DEVICE_LIST);
		List<Device> deviceListFromRepository = deviceRepository.findActiveDevicesOrderedByPinAsc();
		assertEquals(3, deviceListFromRepository.size());
	}
}
