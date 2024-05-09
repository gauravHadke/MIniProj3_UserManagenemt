package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.UserMaster;

@Repository
public interface UserMasterRepo extends JpaRepository<UserMaster, Integer>{

	public UserMaster findByEmailIdAndPassword(String email,String password);
	
	public UserMaster findByEmailId(String email);
}
