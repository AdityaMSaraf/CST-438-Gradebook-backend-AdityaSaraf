package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cst438.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class JunitTestAssignmentController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AssignmentRepository assignmentRepository;

    @MockBean
    private CourseRepository courseRepository;

    @Test
    public void createAssignmentTest() throws Exception{
        AssignmentDTO assignmentDTO = new AssignmentDTO(69, "Recursion", "2023-10-13", "Design & Analysis of Algorithms", 370);
        Assignment assignment = new Assignment();
        assignment.setId(assignmentDTO.id());
        assignment.setName(assignmentDTO.assignmentName());
        assignment.setDueDate(Date.valueOf(assignmentDTO.dueDate()));

        when(assignmentRepository.save(assignment)).thenReturn(assignment);

        MockHttpServletResponse response;

        response = mvc.perform(MockMvcRequestBuilders.post("/assignment")
                .content(asJsonString(assignmentDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    public void getAllAssignmentsTest() throws Exception{

        String instMail = "ybyun@csumb.edu";
        Assignment a1 = new Assignment();
        a1.setName("Recursion");
        a1.setId(1);
        a1.setDueDate(Date.valueOf("2002-10-13"));

        Assignment a2 = new Assignment();
        a2.setName("Android Databases");
        a2.setId(2);
        a2.setDueDate(Date.valueOf("2003-09-06"));

        List<Assignment> l1 = new ArrayList<>();
        l1.add(a1);

        List<Assignment> l2 = new ArrayList<>();
        l2.add(a2);

        List<Assignment> l3 = new ArrayList<>();
        l3.addAll(l1);
        l3.addAll(l2);

        Course c1 = new Course();
        c1.setAssignments(l1);
        c1.setCourse_id(370);
        c1.setSemester("Spring");
        c1.setTitle("Design & Analysis of Algorithms");
        c1.setYear(2023);
        a1.setCourse(c1);

        Course c2 = new Course();
        c2.setAssignments(l2);
        c1.setCourse_id(338);
        c2.setSemester("Spring");
        c2.setTitle("Software Design");
        c2.setYear(2023);
        a2.setCourse(c2);

        when(assignmentRepository.findByEmail(instMail)).thenReturn(l3);

        MockHttpServletResponse response;
        response = mvc.perform(MockMvcRequestBuilders.get("/assignment")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse();


        AssignmentDTO[] DTOS = new AssignmentDTO[l3.size()];
        for(int i = 0; i < l3.size(); i++){
            Assignment ass = l3.get(i);
            DTOS[i] = new AssignmentDTO(
                    ass.getId(),
                    ass.getName(),
                    ass.getDueDate().toString(),
                    ass.getCourse().getTitle(),
                    ass.getCourse().getCourse_id()
            );
        }
        assertEquals(200,response.getStatus());
        assertEquals(2, DTOS.length);
        assertEquals("Software Design", DTOS[1].courseTitle());
        assertEquals("Design & Analysis of Algorithms", DTOS[0].courseTitle());
        assertEquals(a1.toString(), c1.getAssignments().get(0).toString()); //checking if the toString of the assignment matches the one in the list for the course
        assertEquals(a2.toString(), c2.getAssignments().get(0).toString()); //Checking for second course
    }

    private static String asJsonString(final Object obj) {
        try {

            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T fromJsonString(String str, Class<T> valueType) {
        try {
            return new ObjectMapper().readValue(str, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
