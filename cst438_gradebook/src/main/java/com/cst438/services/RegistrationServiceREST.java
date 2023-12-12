package com.cst438.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Enrollment;

import javax.xml.bind.SchemaOutputResolver;

@Service
@ConditionalOnProperty(prefix = "registration", name = "service", havingValue = "rest")
@RestController
public class RegistrationServiceREST implements RegistrationService {


	RestTemplate restTemplate = new RestTemplate();

	@Value("${registration.url}")
	String registration_url;

	public RegistrationServiceREST() {
		System.out.println("REST registration service ");
	}

	@Override
	public void sendFinalGrades(int course_id , FinalGradeDTO[] grades) {
		String URL = registration_url + "/course/" + course_id;
		restTemplate.put(URL, grades);
	}

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;


	/*
	 * endpoint used by registration service to add an enrollment to an existing
	 * course.
	 */
	@PostMapping("/enrollment")
	@Transactional
	public EnrollmentDTO addEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {

		Enrollment e = new Enrollment();
		Course c = courseRepository.findById(enrollmentDTO.courseId()).orElse(null);
		if (c == null) {
			System.out.println("Error! Course not Found!");
			return null;
		}else{
			e.setStudentEmail(enrollmentDTO.studentEmail());
			e.setCourse(c);
			e.setStudentName(enrollmentDTO.studentName());
			enrollmentRepository.save(e);
			EnrollmentDTO eDTO = new EnrollmentDTO(
					e.getId(),
					e.getStudentEmail(),
					e.getStudentName(),
					e.getCourse().getCourse_id()
			);
			return eDTO;
		}
	}

}
