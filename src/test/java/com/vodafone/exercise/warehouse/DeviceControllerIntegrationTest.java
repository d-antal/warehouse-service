package com.vodafone.exercise.warehouse;

import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_ACTIVE;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_ACTIVE_NO_PIN;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_DEFAULT;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_STATUS_BLANK;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_STATUS_NULL;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_TEMPERATURE_NULL;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_TEMPERATURE_TOO_HIGH;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.DEVICE_TEMPERATURE_TOO_LOW;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.ID_1;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.ID_NOT_EXIST;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.WAREHOUSE_URI;
import static com.vodafone.exercise.warehouse.DeviceTestConstants.WAREHOUSE_URI_SLASH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vodafone.exercise.warehouse.controller.DeviceController;
import com.vodafone.exercise.warehouse.exception.DeviceNotFoundException;
import com.vodafone.exercise.warehouse.model.Device;
import com.vodafone.exercise.warehouse.service.DeviceService;

@WebMvcTest(DeviceController.class)
class DeviceControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DeviceService deviceService;
	private static final ObjectMapper OM = new ObjectMapper();

	private static Stream<Device> devicesNotValidBody() {
		return Stream.of(DEVICE_STATUS_NULL, DEVICE_STATUS_BLANK, DEVICE_TEMPERATURE_NULL, DEVICE_TEMPERATURE_TOO_HIGH, DEVICE_TEMPERATURE_TOO_LOW);
	}

	private static Stream<Device> devicesValidBody() {
		return Stream.of(DEVICE_ACTIVE, DEVICE_ACTIVE_NO_PIN);
	}
	
	@Test
	void testGetAllActiveDevicesOrderedByPin() throws Exception {
		List<Device> devices = Arrays.asList(DEVICE_ACTIVE);
		when(deviceService.findAllActiveDevicesOrderedByPin()).thenReturn(devices);
		mockMvc.perform(get(WAREHOUSE_URI).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].status", Matchers.is(DEVICE_ACTIVE.getStatus())))
				.andExpect(jsonPath("$[0].temperature", Matchers.is(Integer.valueOf(DEVICE_ACTIVE.getTemperature()))));
		verify(deviceService, times(1)).findAllActiveDevicesOrderedByPin();
	}

	@Test
	void tesGetDeviceById() throws Exception {
		when(deviceService.findDeviceById(ID_1)).thenReturn(DEVICE_ACTIVE);
		mockMvc.perform(get(WAREHOUSE_URI_SLASH + ID_1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())		
				.andExpect(jsonPath("$.id").value(DEVICE_ACTIVE.getId()))
				.andExpect(jsonPath("$.status").value(DEVICE_ACTIVE.getStatus()))
				.andExpect(jsonPath("$.temperature").value(Integer.valueOf(DEVICE_ACTIVE.getTemperature())))
				.andExpect(jsonPath("$.pin").value(DEVICE_ACTIVE.getPin()));
		verify(deviceService, times(1)).findDeviceById(ID_1);
	}
	
	@Test
	void tesGetDeviceByIdWhenDeviceNotFound() throws Exception {
		when(deviceService.findDeviceById(ID_NOT_EXIST)).thenThrow(DeviceNotFoundException.class);
		mockMvc.perform(get(WAREHOUSE_URI_SLASH + ID_NOT_EXIST).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());	
		verify(deviceService, times(1)).findDeviceById(ID_NOT_EXIST);
	}
	
	@ParameterizedTest
	@MethodSource("devicesValidBody")
	void testAddDeviceWhenRequestBodyIsValid(Device device) throws Exception {
		when(deviceService.addDevice(device)).thenReturn(device);
		mockMvc.perform(post(WAREHOUSE_URI).content(OM.writeValueAsString(device)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(device.getId()))
				.andExpect(jsonPath("$.status").value(device.getStatus()))
				.andExpect(jsonPath("$.temperature").value(Integer.valueOf(device.getTemperature())))				
				.andExpect(device.getPin() == null ? jsonPath("$.pin").isEmpty() : jsonPath("$.pin").value(device.getPin()));
				
		verify(deviceService, times(1)).addDevice(device);
	}
	
	@ParameterizedTest
	@MethodSource("devicesNotValidBody")
	void testAddDeviceWhenRequestBodyIsNotValid(Device device) throws Exception {		
		mockMvc.perform(post(WAREHOUSE_URI).content(OM.writeValueAsString(device)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		verify(deviceService, never()).addDevice(any(Device.class));
	}
	
	@Test
	void testUpdateDevice() throws Exception {
		when(deviceService.updateDevice(ID_1, DEVICE_DEFAULT)).thenReturn(DEVICE_DEFAULT);
		mockMvc.perform(put(WAREHOUSE_URI_SLASH + ID_1).content(OM.writeValueAsString(DEVICE_DEFAULT)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())		
				.andExpect(jsonPath("$.id").value(DEVICE_DEFAULT.getId()))
				.andExpect(jsonPath("$.status").value(DEVICE_DEFAULT.getStatus()))
				.andExpect(jsonPath("$.temperature").value(Integer.valueOf(DEVICE_DEFAULT.getTemperature())))
				.andExpect(jsonPath("$.pin").value(DEVICE_DEFAULT.getPin()));	
		verify(deviceService, times(1)).updateDevice(ID_1, DEVICE_DEFAULT);		
	}
	
	@ParameterizedTest
	@MethodSource("devicesNotValidBody")	
	 void testUpdateDeviceWhenRequestBodyIsNotValid(Device device) throws Exception {		
		mockMvc.perform(put(WAREHOUSE_URI_SLASH + device.getId()).content(OM.writeValueAsString(device)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());		
		verify(deviceService, never()).updateDevice(any(Long.class), any(Device.class));
	}
	
	@Test
	 void testUpdateDeviceWhenDeviceNotFound() throws Exception {
		when(deviceService.updateDevice(ID_NOT_EXIST, DEVICE_DEFAULT)).thenThrow(DeviceNotFoundException.class);
		mockMvc.perform(put(WAREHOUSE_URI_SLASH + ID_NOT_EXIST).content(OM.writeValueAsString(DEVICE_DEFAULT)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		verify(deviceService, times(1)).updateDevice(ID_NOT_EXIST, DEVICE_DEFAULT);
	}
	
	@Test
	void testRemoveDevice() throws Exception {
		when(deviceService.removeDevice(ID_1)).thenReturn(ID_1);
		mockMvc.perform(delete(WAREHOUSE_URI_SLASH + ID_1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		verify(deviceService, times(1)).removeDevice(ID_1);
	}

	@Test
	void testRemoveDeviceWhenDeviceNotFound() throws Exception {
		when(deviceService.removeDevice(ID_NOT_EXIST)).thenThrow(DeviceNotFoundException.class);
		mockMvc.perform(delete(WAREHOUSE_URI_SLASH + ID_NOT_EXIST).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		verify(deviceService, times(1)).removeDevice(ID_NOT_EXIST);
	}

}
