package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.Student;
import com.example.repo.StudentRepo;

@Service
public class StudentService {

    @Autowired
    StudentRepo studentRepo;
	@Autowired
	JWTService jwtService;

	@Autowired
	AuthenticationManager authenticationManager;
    
    private BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(12);

    public Student saveStudent(Student student) {
    	student.setPassword(bCrypt.encode(student.getPassword()));
        return studentRepo.save(student);
    }
    
    public List<Student> getAllStudent(){
    	return studentRepo.findAll();
    }
    
    public void deleteStudent(int id)   
    {  
    	studentRepo.deleteById(id);  
    } 
    

	public Student updateStudent(int studentid, Student student) {
		Optional<Student> checkIdIsPresntOrNot = studentRepo.findById(studentid);
		if (checkIdIsPresntOrNot.isPresent()) {
			 Student studentToUpdate = checkIdIsPresntOrNot.get();
	            studentToUpdate.setFullName(student.getFullName());
	            studentToUpdate.setEmail(student.getEmail());
	            studentToUpdate.setPassword(bCrypt.encode(student.getPassword()));
	            
	         return studentRepo.save(studentToUpdate);
	        } else {
	           
	            throw new RuntimeException("Student not found");
	        }
			
		
	}

	public Student findByEmail(String userEmail) {
		// TODO Auto-generated method stub
		return studentRepo.findByEmail(userEmail);
	}

	public String verify(Student student) {
		Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(student.getEmail(),student.getPassword()));
		if(authentication.isAuthenticated())
			return jwtService.generateToken(student.getEmail());

		return "failure";
	}
}

