package com.vodafone.exercise.warehouse;

import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_LIST;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.ID_1;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.ID_2;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.ID_NOT_EXIST;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.PIN_1;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.PIN_2;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.STATUS_ACTIVE;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.STATUS_DEFAULT;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.TEMPERATURE_ACTIVE;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.TEMPERATURE_DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vodafone.exercise.warehouse.exception.DeviceNotFoundException;
import com.vodafone.exercise.warehouse.model.Device;
import com.vodafone.exercise.warehouse.repository.DeviceRepository;
import com.vodafone.exercise.warehouse.service.DeviceService;
import com.vodafone.exercise.warehouse.service.DeviceServiceImpl;

class DeviceServiceTest {

	@Mock
	private DeviceRepository deviceRepository;
	private DeviceService deviceService;
	private static Device DEFAULT_DEVICE;
	private static Device UPDATED_DEVICE;
	private static Device UPDATED_DEVICE_NO_PIN;
	private static Device ACTIVE_DEVICE;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		deviceService = new DeviceServiceImpl(deviceRepository);
		DEFAULT_DEVICE = new Device(ID_1, STATUS_DEFAULT, TEMPERATURE_DEFAULT, PIN_1);
		UPDATED_DEVICE = new Device(ID_1, STATUS_ACTIVE, TEMPERATURE_ACTIVE, PIN_1);
		UPDATED_DEVICE_NO_PIN = new Device(ID_1, STATUS_ACTIVE, TEMPERATURE_ACTIVE, null);
		ACTIVE_DEVICE = new Device(ID_2, STATUS_DEFAULT, TEMPERATURE_DEFAULT, PIN_2);
	}

	@Test
	void testFindDeviceById() {
		when(deviceRepository.findById(ID_1)).thenReturn(Optional.of(DEFAULT_DEVICE));
		Device device = deviceService.findDeviceById(ID_1);
		assertEquals(DEFAULT_DEVICE, device);
		verify(deviceRepository, times(1)).findById(ID_1);
	}

	@Test
	void testFindDeviceByIdWhenDeviceNotFound() {
		when(deviceRepository.findById(ID_NOT_EXIST)).thenReturn(Optional.empty());
		assertThrows(DeviceNotFoundException.class, () -> deviceService.findDeviceById(ID_NOT_EXIST));
		verify(deviceRepository, times(1)).findById(ID_NOT_EXIST);
	}

	@Test
	void testFindDeviceByIdIsNull() {
		when(deviceRepository.findById(null)).thenThrow(new IllegalArgumentException());
		assertThrows(IllegalArgumentException.class, () -> deviceService.findDeviceById(null));
		verify(deviceRepository, times(1)).findById(null);
	}

	@Test
	void testAddDevice() {
		when(deviceRepository.save(ACTIVE_DEVICE)).thenReturn(ACTIVE_DEVICE);
		Device device = deviceService.addDevice(ACTIVE_DEVICE);
		assertEquals(ACTIVE_DEVICE, device);
		verify(deviceRepository, times(1)).save(ACTIVE_DEVICE);
	}

	@Test
	void testUpdateDeviceWhenDevicetExist() throws DeviceNotFoundException {
		when(deviceRepository.findById(ID_1)).thenReturn(Optional.of(DEFAULT_DEVICE));
		when(deviceRepository.save(UPDATED_DEVICE)).thenReturn(UPDATED_DEVICE);
		Device updatedDevice = deviceService.updateDevice(ID_1, UPDATED_DEVICE);
		assertEquals(TEMPERATURE_ACTIVE, updatedDevice.getTemperature());
		assertEquals(STATUS_ACTIVE, updatedDevice.getStatus());
		verify(deviceRepository, times(1)).findById(ID_1);
		verify(deviceRepository, times(1)).save(any(Device.class));
	}

	@Test
	void testUpdateDeviceWhenNoPin() throws DeviceNotFoundException {
		when(deviceRepository.findById(ID_1)).thenReturn(Optional.of(DEFAULT_DEVICE));
		when(deviceRepository.save(UPDATED_DEVICE)).thenReturn(UPDATED_DEVICE);
		Device updatedDevice = deviceService.updateDevice(ID_1, UPDATED_DEVICE_NO_PIN);
		assertEquals(TEMPERATURE_ACTIVE, updatedDevice.getTemperature());
		assertEquals(STATUS_ACTIVE, updatedDevice.getStatus());
		assertNotNull(updatedDevice.getPin());
		verify(deviceRepository, times(1)).findById(ID_1);
		verify(deviceRepository, times(1)).save(any(Device.class));
	}

	@Test
	void testUpdateDeviceWhenDeviceNotExist() throws DeviceNotFoundException {
		when(deviceRepository.findById(ID_NOT_EXIST)).thenReturn(Optional.empty());
		assertThrows(DeviceNotFoundException.class, () -> deviceService.updateDevice(ID_NOT_EXIST, ACTIVE_DEVICE));
		verify(deviceRepository, times(1)).findById(ID_NOT_EXIST);
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	void testUpdateDeviceWhenIdIsNull() throws DeviceNotFoundException {
		when(deviceRepository.findById(null)).thenThrow(new IllegalArgumentException());
		assertThrows(IllegalArgumentException.class, () -> deviceService.updateDevice(null, ACTIVE_DEVICE));
		verify(deviceRepository, times(1)).findById(null);
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	void testRemoveDeviceWhenDeviceExist() throws DeviceNotFoundException {
		when(deviceRepository.findById(ID_1)).thenReturn(Optional.of(DEFAULT_DEVICE));
		long deviceId = deviceService.removeDevice(ID_1);
		assertEquals(ID_1, deviceId);
		verify(deviceRepository, times(1)).findById(ID_1);
		verify(deviceRepository, times(1)).delete(DEFAULT_DEVICE);
	}

	@Test
	void testRemoveDeviceWhenDeviceNotExist() throws DeviceNotFoundException {
		when(deviceRepository.findById(ID_NOT_EXIST)).thenReturn(Optional.empty());
		assertThrows(DeviceNotFoundException.class, () -> deviceService.removeDevice(ID_NOT_EXIST));
		verify(deviceRepository, times(1)).findById(ID_NOT_EXIST);
		verify(deviceRepository, never()).delete(any(Device.class));
	}

	@Test
	void testRemoveDeviceWhenIdIsNull() throws DeviceNotFoundException {
		when(deviceRepository.findById(null)).thenThrow(new IllegalArgumentException());
		assertThrows(IllegalArgumentException.class, () -> deviceService.removeDevice(null));
		verify(deviceRepository, times(1)).findById(null);
		verify(deviceRepository, never()).delete(any(Device.class));
	}

	@Test
	void testFindAllActiveDevices() {		
		List<Device> orderedActiveDevices = DEVICE_LIST.stream().sorted(Comparator.comparingInt(Device::getPin)).filter(d -> d.getStatus().equals(STATUS_ACTIVE)).collect(Collectors.toList());
		when(deviceRepository.findActiveDevicesOrderedByPinAsc()).thenReturn(orderedActiveDevices);
		List<Device> deviceListFromRepository = deviceService.findAllActiveDevicesOrderedByPin();
		deviceListFromRepository.forEach(d -> assertEquals(STATUS_ACTIVE, d.getStatus()));
		deviceListFromRepository.forEach(d -> assertTrue(d.getPin() >= PIN_1));
		assertEquals(3, deviceListFromRepository.size());
		assertEquals(4, DEVICE_LIST.size());
		verify(deviceRepository, times(1)).findActiveDevicesOrderedByPinAsc();
	}

}
