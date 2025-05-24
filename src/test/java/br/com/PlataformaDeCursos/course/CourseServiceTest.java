package br.com.PlataformaDeCursos.course;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private final CourseService courseService = new CourseService(courseRepository);

    @Test
    void CreateCourse__withValidData_shouldSaveAndReturnCourse() {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("java-basic");
        newCourseDTO.setName("Java Básico");
        newCourseDTO.setDescription("Curso básico de Java");
        newCourseDTO.setInstructorEmail("instructor@email.com");
        Course expectedCourse = new Course("java-basic", "Java Básico", "Curso básico de Java", "instructor@email.com",
                Status.ACTIVE);

        when(courseRepository.save(any(Course.class))).thenReturn(expectedCourse);

        Course createdCourse = courseService.createCourse(newCourseDTO);

        assertThat(createdCourse).isNotNull();
        assertThat(createdCourse.getCode()).isEqualTo("java-basic");
        assertThat(createdCourse.getName()).isEqualTo("Java Básico");
        assertThat(createdCourse.getDescription()).isEqualTo("Curso básico de Java");
        assertThat(createdCourse.getInstructorEmail()).isEqualTo("instructor@email.com");
        assertThat(createdCourse.getStatus()).isEqualTo(Status.ACTIVE);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void DeactivateCourse__withActiveCourse_shouldSetInactiveStatusAndSave() {
        // Arrange
        Course activeCourse = new Course("java-basic", "Java Básico", "Curso básico de Java",
                "instructor@email.com",
                Status.ACTIVE);

        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course course = invocation.getArgument(0);
            course.setInactivationAt(LocalDateTime.now());
            return course;
        });

        Course deactivatedCourse = courseService.deactivateCourse(activeCourse);

        assertThat(deactivatedCourse.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(deactivatedCourse.getInactivationAt()).isNotNull();

        verify(courseRepository, times(1)).save(activeCourse);
    }

    @Test
    void DeactivateCourse__withAlreadyInactiveCourse_shouldNotChangeStatus() {
        Course inactiveCourse = new Course("java-basic", "Java Básico", "Curso básico de Java", "instructor@email.com", Status.INACTIVE);
        inactiveCourse.setInactivationAt(LocalDateTime.now());

        when(courseRepository.save(any(Course.class))).thenReturn(inactiveCourse);

        Course resultCourse = courseService.deactivateCourse(inactiveCourse);

        assertThat(resultCourse.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(resultCourse.getInactivationAt()).isNotNull();
        verify(courseRepository, times(1)).save(inactiveCourse);
    }
}
