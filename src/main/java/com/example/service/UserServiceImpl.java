package com.example.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.binding.ActivateAccount;
import com.example.binding.LogIn;
import com.example.binding.User;
import com.example.entity.UserMaster;
import com.example.repo.UserMasterRepo;
import com.example.util.EmailUtils;

@Service
public class UserServiceImpl implements UserService{
	
	private UserMasterRepo repo;
	
	@Autowired
	private EmailUtils emaiUtils;
	
	@Autowired
	public void setRepo(UserMasterRepo repo) {
		this.repo = repo;
	}

	@Override
	public boolean saveUser(User user) {
		UserMaster userMaster = new UserMaster();
		BeanUtils.copyProperties(user, userMaster);
		System.out.println(userMaster);
		userMaster.setAccStatus("In-Active");
		userMaster.setPassword(generateRandomPassword());
		//you can check here whether the email id is duplicate or not
		UserMaster save = repo.save(userMaster);
		
		//to do : mail send logic 
		String subject = "Your Registration Success.. ";
		String fileName = "REG-EMAIL-BODY.txt";//it use to reg user and send pwd by mail
		String body = readEmailBody(userMaster.getFullName(),
									   userMaster.getPassword(),fileName);
		
		emaiUtils.sendEmail(user.getEmailId(), subject, body);
		
		return save.getUserId()!=null;
	}

	@Override
	public boolean activateAccount(ActivateAccount account) {
		UserMaster userMaster = new UserMaster();
		userMaster.setEmailId(account.getEmail());
		userMaster.setPassword(account.getTemporaryPassword());
		
		//select * from usermaster where emailId=? and password=?
		Example<UserMaster> of = Example.of(userMaster);
		
		List<UserMaster> findAll = repo.findAll(of);
		
		if(findAll.isEmpty()) {
			return false;
		}else {
			UserMaster entity = findAll.get(0);
			entity.setAccStatus("Active");
			entity.setPassword(account.getNewPassword());
			repo.save(entity);
			return true;
		}
	}

	@Override
	public List<User> getAllUSers() {
		List<User> users = new ArrayList<>();
		List<UserMaster> findAll = repo.findAll();
		for(UserMaster entities : findAll) {
			User user = new User();
			BeanUtils.copyProperties(entities, user);
			users.add(user);
		}		
		return users;
	}

	@Override
	public User getUserById(Integer userId) {
		// TODO Auto-generated method stub
		User user = new User();
		Optional<UserMaster> findById = repo.findById(userId);
		if(findById.isPresent()) {
			UserMaster userMaster = findById.get();
			BeanUtils.copyProperties(userMaster, user);
		}else {
			System.out.println("Id not found"+"Please search valid id");
			return null;
		}
		return user;
	}

	
	@Override
	public boolean updateUser(User user) {
		// TODO Auto-generated method stub
		UserMaster userMaster = new UserMaster();
		BeanUtils.copyProperties(user, userMaster);
		UserMaster save = repo.save(userMaster);
		return save.getUserId()!=null;
	}

	@Override
	public boolean deleteUser(Integer userId) {
		boolean status = false;
		try {
			repo.deleteById(userId);
			return status = true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public boolean statusChange(Integer userId, String status) {
		// TODO Auto-generated method stub
		Optional<UserMaster> findById = repo.findById(userId);
		if(findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setAccStatus(status);
			repo.save(userMaster);
			return true;
		}
		return false;
	}

	@Override
	public String logIn(LogIn login) {
		// TODO Auto-generated method stub
		UserMaster entity = repo.findByEmailIdAndPassword(login.getEmailId(), login.getPassword());
		if(entity == null) {
			return "Invalid credentials";
		}
		
		if(entity.getAccStatus().equals("Active")) {
			return "Log in successFully...";
		}else {
			return "not Activated";	
		}
	}

	
	@Override
	public String forgotPassword(String email) {
		UserMaster userMaster = repo.findByEmailId(email);
		
		//send pwd to user in email
		 String subject = "Forgot password";
		 String fileName = "RECOVER-PWD.txt";
		 String body = readEmailBody(userMaster.getEmailId(), 
				 					userMaster.getPassword(), fileName); 
		 
		 boolean sendEmail = emaiUtils.sendEmail(email, subject, body);
		
		 if(sendEmail) {
			 return "Password send to your registered email";
		 }
		 
		 return null;
	}
	

	//to check duplicate email if not duplicate then allow to register (for that logic)
	public User getUserByEmailId(String email) {
		// TODO Auto-generated method stub
		User user = new User();
		UserMaster findByEmailId = repo.findByEmailId(email);
		if(findByEmailId!=null) {
			BeanUtils.copyProperties(findByEmailId, user);
			return user;
		}
		return null;
	}

	
	public String generateRandomPassword() {
	    String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
	    String numbers = "0123456789";

	    String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
	    
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();

	    int length = 10;

	    for(int i = 0; i < length; i++) {
	      int index = random.nextInt(alphaNumeric.length());
	      char randomChar = alphaNumeric.charAt(index);
	      sb.append(randomChar);
	    }
	    String randomString = sb.toString();
	    System.out.println("Random String is: " + randomString);
	    
	    return randomString;
	}
	
	  
	private String readEmailBody(String fullName, String pwd, String filename){
	//	String fileName = "REG-EMAIL-BODY.txt";
		//url will get from properties file
		String url="";
		String mailBody= null;
		
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			StringBuffer buffer = new StringBuffer();
		
			String line = bufferedReader.readLine();
			while(line!=null) {
				//process data
				buffer.append(line);
				line = bufferedReader.readLine(); // continue loop until line null to read
				
			}
			//stored bufferReader content to string coz for dynamic msg we need to replace
			//file dynamic data user to user
			mailBody = buffer.toString();
			mailBody = mailBody.replaceAll("\\{FULL_NAME\\}", fullName);
			//mailBody.replace("{FULL_NAME}", fullName);
			mailBody = mailBody.replaceAll("\\{TEMP_PWD\\}", pwd);
			mailBody = mailBody.replaceAll("\\{URL\\}", url);
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
		return mailBody;
	}

}
