package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.binding.ActivateAccount;
import com.example.binding.LogIn;
import com.example.binding.User;

@Service
public interface UserService {

	public boolean saveUser(User user);
	
	public boolean activateAccount(ActivateAccount account);
	
	public List<User> getAllUSers();
	
	public User getUserById(Integer userId);
	
	public boolean updateUser(User user);
	
	public boolean deleteUser(Integer userId);
	
	public boolean statusChange(Integer userId , String status);
	
	public String logIn(LogIn login);
	
	public String forgotPassword(String email);
	
	public User getUserByEmailId(String email);
	
}
