package com.vodafone.exercise.warehouse;

import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_ACTIVE;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_DEFAULT;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.EXPECTED_EXCEPTION_NOT_FOUND;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.ID_1;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.ID_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vodafone.exercise.warehouse.controller.DeviceController;
import com.vodafone.exercise.warehouse.exception.DeviceNotFoundException;
import com.vodafone.exercise.warehouse.model.Device;
import com.vodafone.exercise.warehouse.service.DeviceService;

class DeviceControllerTest {

	@InjectMocks
	private DeviceController deviceController;

	@Mock
	private DeviceService deviceService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAllActiveDevicesOrderedByPin() {
		when(deviceService.findAllActiveDevicesOrderedByPin()).thenReturn(new ArrayList<Device>());
		deviceController.getAllActiveDevicesOrderedByPin();
		verify(deviceService, times(1)).findAllActiveDevicesOrderedByPin();
	}

	@Test
	void tesGetDeviceById() throws Exception {
		when(deviceService.findDeviceById(ID_1)).thenReturn(DEVICE_ACTIVE);
		deviceController.getDeviceById(ID_1);
		verify(deviceService, times(1)).findDeviceById(ID_1);
	}

	@Test
	void tesGetDeviceByIdWhenDeviceNotFound() throws Exception {
		when(deviceService.findDeviceById(ID_NOT_EXIST)).thenThrow(DeviceNotFoundException.class);
		assertThrows(DeviceNotFoundException.class, () -> {
			deviceController.getDeviceById(ID_NOT_EXIST);
		}, EXPECTED_EXCEPTION_NOT_FOUND);
		verify(deviceService, times(1)).findDeviceById(ID_NOT_EXIST);
	}

	@Test
	void testAddDevice() throws Exception {
		when(deviceService.addDevice(DEVICE_DEFAULT)).thenReturn(DEVICE_DEFAULT);
		deviceController.addDevice(DEVICE_DEFAULT);
		verify(deviceService, times(1)).addDevice(DEVICE_DEFAULT);
	}

	@Test
	void testUpdateDevice() throws Exception {
		when(deviceService.updateDevice(ID_1, DEVICE_ACTIVE)).thenReturn(DEVICE_ACTIVE);
		deviceController.updateDevice(ID_1, DEVICE_ACTIVE);
		verify(deviceService, times(1)).updateDevice(ID_1, DEVICE_ACTIVE);
	}

	@Test
	void testUpdateDeviceWhenDeviceNotFound() throws Exception {
		when(deviceService.updateDevice(ID_NOT_EXIST, DEVICE_DEFAULT)).thenThrow(DeviceNotFoundException.class);
		assertThrows(DeviceNotFoundException.class, () -> {
			deviceController.updateDevice(ID_NOT_EXIST, DEVICE_DEFAULT);
		}, EXPECTED_EXCEPTION_NOT_FOUND);
		verify(deviceService, times(1)).updateDevice(ID_NOT_EXIST, DEVICE_DEFAULT);
	}

	@Test
	void testRemoveDevice() throws Exception {
		when(deviceService.removeDevice(ID_1)).thenReturn(ID_1);
		deviceController.removeDevice(ID_1);
		verify(deviceService, times(1)).removeDevice(ID_1);
	}

	@Test
	void testRemoveDeviceWhenDeviceNotFound() throws Exception {
		when(deviceService.removeDevice(ID_NOT_EXIST)).thenThrow(DeviceNotFoundException.class);
		assertThrows(DeviceNotFoundException.class, () -> {
			deviceController.removeDevice(ID_NOT_EXIST);
		}, EXPECTED_EXCEPTION_NOT_FOUND);
		verify(deviceService, times(1)).removeDevice(ID_NOT_EXIST);
	}

}
