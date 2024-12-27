package com.example.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.model.Student;
import com.example.service.StudentService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/student") 
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping("/saveStudent") 
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student save = studentService.saveStudent(student);
        return ResponseEntity.ok(save);
        
    }
    
    @GetMapping("/getAllStudent")
    public ResponseEntity<Student> findOwnData() {
        // Get the authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        
        // Find student by email
        Student student = studentService.findByEmail(userEmail);
        
        if (student == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(student);
    }
    
    @DeleteMapping("/delete/{studentid}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("studentid") int studentid) {
        // Get the authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        
        Student student = studentService.findByEmail(userEmail);
        
        if (student != null && student.getId() == studentid) {
            studentService.deleteStudent(studentid);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build(); 
        }
    }
    
    @PutMapping("/update/{studentid}")
    public ResponseEntity<Student> updateStudent(@PathVariable("studentid") int studentid, @RequestBody Student studentDemo) {
        // Get the authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        
        Student student = studentService.findByEmail(userEmail);
        
        if (student != null && student.getId() == studentid) {
            Student updatedStudent = studentService.updateStudent(studentid, studentDemo);
            return ResponseEntity.ok(updatedStudent);
        } else {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
    }
    
    @GetMapping("/")
    public String greet(HttpServletRequest request) {
    	return "Hello world " + request.getSession().getId();
    }

    @PostMapping("/login")
    public String login(@RequestBody Student student){
        System.out.println(student.toString());
        return studentService.verify(student);
    }
    
}

