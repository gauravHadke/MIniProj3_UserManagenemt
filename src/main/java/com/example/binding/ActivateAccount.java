package com.example.binding;

import lombok.Data;

@Data
public class ActivateAccount {
	
	private String email;
	private String newPassword;
	private String temporaryPassword;
	private String confirmPassword;
	
}
