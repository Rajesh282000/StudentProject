package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.model.Student;
import com.example.model.UserPrincipal;
import com.example.repo.StudentRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private StudentRepo studentRepo;

    @Override
    public UserDetails loadUserByUsername(String email){
     Student  student = studentRepo.findByEmail(email);
     if(student == null) {
    	throw new UsernameNotFoundException("Student not found ");
     }
     	return new UserPrincipal(student);
    }
}
