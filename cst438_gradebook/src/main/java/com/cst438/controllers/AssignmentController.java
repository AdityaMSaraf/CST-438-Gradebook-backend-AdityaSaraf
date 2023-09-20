package com.cst438.controllers;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
@CrossOrigin
public class AssignmentController {

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    CourseRepository courseRepository;



    @PostMapping("/assignment")
    public int createAssignment(@RequestBody AssignmentDTO assignmentDTO){
        Assignment assignment = new Assignment();
//        assignment.setId(assignmentDTO.id());
        assignment.setCourse(courseRepository.findById(assignmentDTO.courseId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found")));
        assignment.setName(assignmentDTO.assignmentName());
        assignment.setDueDate(Date.valueOf(assignmentDTO.dueDate()));
        assignmentRepository.save(assignment);
        return assignment.getId();
    }

    @GetMapping("/assignment/{id}")
    public AssignmentDTO getAssignment(@PathVariable("id") int id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found"));

        return new AssignmentDTO(
                assignment.getId(),
                assignment.getName(),
                assignment.getDueDate().toString(),
                assignment.getCourse().getTitle(),
                assignment.getCourse().getCourse_id()
        );
    }

    @PutMapping("/assignment/{id}")
    public void updateAssignment(@RequestBody AssignmentDTO assignmentDTO){
        Assignment assignment = new Assignment();
        assignment.setId(assignmentDTO.id());
        assignment.setCourse(courseRepository.findById(assignmentDTO.courseId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found")));
        assignment.setName(assignmentDTO.assignmentName());
        assignment.setDueDate(Date.valueOf(assignmentDTO.dueDate()));
        assignmentRepository.save(assignment);
    }



    @DeleteMapping("/assignment/{id}")
    public void deleteAssignment(@PathVariable("id") int id,
    @RequestParam(value = "force") Optional<Boolean> force){
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found"));


        if(force.isEmpty() && !assignment.getCourse().getEnrollments().isEmpty()){
            throw(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Grades exist for this assignment, please use force to confirm deletion"));
        } else if ((force.isPresent() && force.get()) || assignment.getCourse().getEnrollments().isEmpty()) {
            assignmentRepository.delete(assignment);
        }
    }

    @GetMapping("/assignment")
    public AssignmentDTO[] getAllAssignmentsForInstructor() {
        // get all assignments for this instructor
        String instructorEmail = "dwisneski@csumb.edu";  // username (should be instructor's email)
        List<Assignment> assignments = assignmentRepository.findByEmail(instructorEmail);
        AssignmentDTO[] result = new AssignmentDTO[assignments.size()];
        for (int i = 0; i < assignments.size(); i++) {
            Assignment as = assignments.get(i);
            AssignmentDTO dto = new AssignmentDTO(
                    as.getId(),
                    as.getName(),
                    as.getDueDate().toString(),
                    as.getCourse().getTitle(),
                    as.getCourse().getCourse_id());
            result[i] = dto;
        }
        return result;
    }

}
