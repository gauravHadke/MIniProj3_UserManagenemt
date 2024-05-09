package com.example.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "USER_MASTER")
public class UserMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String fullName;
	private String emailId;
	private Long mobile;
	private Character gender;
	private LocalDate dob;
	private Long ssn;
	private String accStatus;
	private String password;
	@CreationTimestamp  //to genrerate date
	private LocalDate createdDate;
	@UpdateTimestamp
	private LocalDate updatedDate;
	private String createdBy;
	private String updatedBy;
}
