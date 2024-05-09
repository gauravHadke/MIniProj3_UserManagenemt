package com.example.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {

	public String fullName;
	public String emailId;
	public Long mobile;
	public Character gender;
	public LocalDate dob;
	public Long ssn;
}
