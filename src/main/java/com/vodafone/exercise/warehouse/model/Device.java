package com.vodafone.exercise.warehouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
@Generated
@Entity
@Table(name = "device")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "status", nullable = false)
	@NotBlank
	private String status;

	@Column(name = "temperature", nullable = false)
	@NotNull()
	@Min(value = -1)
	@Max(value = 10)
	private Byte temperature;

	@Column(name = "pin", nullable = true)
	@Min(value = 0000001)
	@Max(value = 9999999)
	private Integer pin;

}
