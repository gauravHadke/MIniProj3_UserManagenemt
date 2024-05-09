package com.example.restCOntrolelr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.binding.ActivateAccount;
import com.example.binding.LogIn;
import com.example.binding.User;
import com.example.service.UserService;

@RestController
public class UserController {

	private UserService service;
	
	@Autowired
	public void setService(UserService service) {
		this.service = service;
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> regUSer(@RequestBody User user){
		boolean saveUser = service.saveUser(user);
		if(saveUser) {
			return new ResponseEntity<>("Registration Success",HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>("Registration Failed.",HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/activate")
	public ResponseEntity<String> accntActivate(@RequestBody ActivateAccount activateAcc){
		boolean activateAccount = service.activateAccount(activateAcc);
		if(activateAccount){
			return new ResponseEntity<>("Account Activated",HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>("Invalid Password",HttpStatus.BAD_REQUEST);
		}  
	} 
	
	@GetMapping("/getAllUsers")
	public ResponseEntity<List<User>> getUsers(){
		List<User> allUSers = service.getAllUSers();
		return new ResponseEntity<>(allUSers,HttpStatus.OK);
	}
	
	@GetMapping("/getuser/{userId}")
	public ResponseEntity<User> getUser(@PathVariable("userId") Integer id){
		User userById = service.getUserById(id);
		return new ResponseEntity<>(userById,HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteUser/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") Integer uid){
		boolean deleteUser = service.deleteUser(uid);
		if(deleteUser) {
			return new ResponseEntity<>("Deleted successfully",HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Failed...",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/statusChange/{userId}/{status}")
	public ResponseEntity<String> changeStatus(@PathVariable("userId") Integer userId,
											   @PathVariable("status") String status){
		boolean statusChange = service.statusChange(userId, status);
 		if(statusChange) {
			return new ResponseEntity<>("Status changed",HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Status Failed to change", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> logIn(@RequestBody LogIn logIn){
		String login = service.logIn(logIn);
		return new ResponseEntity<>(login,HttpStatus.OK);
	}
	
	@GetMapping("/forgotPass/{emailId}")
	public ResponseEntity<String> forgotPassword(@PathVariable("emailId") String mail){
		String forgotPassword = service.forgotPassword(mail);
		return new ResponseEntity<>(forgotPassword,HttpStatus.OK);
	}
}
