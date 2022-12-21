package com.vodafone.exercise.warehouse.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vodafone.exercise.warehouse.exception.DeviceNotFoundException;
import com.vodafone.exercise.warehouse.model.Device;
import com.vodafone.exercise.warehouse.service.DeviceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/warehouse")
public class DeviceController {

	private static final String NOT_FOUND = "Device not found";
	private static final String INVALID_ID = "Invalid id was provided";
	private static final String FOUND_BY_ID = "Device was found by id";
	private static final String SERVER_ERROR = "The Warehouse service failed to process the request";
	private final DeviceService deviceService;

	@Operation(summary = "Get all active devices ordered by pin code")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of the active Devices ordered by the pin code ", content = {
							@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Device.class))) }),
							@ApiResponse(responseCode = "500", description = SERVER_ERROR, content = @Content) })
	@GetMapping("/devices")
	public List<Device> getAllActiveDevicesOrderedByPin() {
		return deviceService.findAllActiveDevicesOrderedByPin();
	}

	@Operation(summary = "Get a device by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = FOUND_BY_ID, content = { @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Device.class)) }),
							@ApiResponse(responseCode = "400", description = INVALID_ID, content = @Content), 
							@ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content),
							@ApiResponse(responseCode = "500", description = SERVER_ERROR, content = @Content) })
	@GetMapping("/devices/{id}")
	public Device getDeviceById(@PathVariable(value = "id") Long deviceId) throws DeviceNotFoundException {
		return deviceService.findDeviceById(deviceId);
	}

	@Operation(summary = "Save a new device")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Device is saved", content = { @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Device.class)) }),
							@ApiResponse(responseCode = "400", description = "Invalid body", content = @Content),
							@ApiResponse(responseCode = "500", description = SERVER_ERROR, content = @Content)})
	@PostMapping("/devices")
	@ResponseStatus(HttpStatus.CREATED)
	public Device addDevice(@Valid @RequestBody Device device) {
		return deviceService.addDevice(device);
	}

	@Operation(summary = "Update a device")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Device is updated", content = { @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Device.class)) }),
							@ApiResponse(responseCode = "400", description = "Invalid body or id", content = @Content), 
							@ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content),
							@ApiResponse(responseCode = "500", description = SERVER_ERROR, content = @Content) })
	@PutMapping("/devices/{id}")
	public Device updateDevice(@PathVariable(value = "id") Long deviceId, @Valid @RequestBody Device newDevice) throws DeviceNotFoundException {
		return deviceService.updateDevice(deviceId, newDevice);
	}

	@Operation(summary = "Remove a device")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Device is removed", content = { @Content(mediaType = APPLICATION_JSON_VALUE) }),
							@ApiResponse(responseCode = "400", description = INVALID_ID, content = @Content), 
							@ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content),
							@ApiResponse(responseCode = "500", description = SERVER_ERROR, content = @Content)})
	@DeleteMapping("/devices/{id}")
	public void removeDevice(@PathVariable(value = "id") Long deviceId) throws DeviceNotFoundException {
		deviceService.removeDevice(deviceId);
	}
}
