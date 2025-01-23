package br.com.alura.ProjetoAlura.course;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//Embora o template inicial que há em User não tenha serviços eu optei por separar as responsabilidades de lógica da controller em uma service, por ser um padrão que estou habituado e acredito ser uma boa prática, deixando para a controller apenas a lógica de validação dos métodos.
@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public Course createCourse(NewCourseDTO dto) {
        Course course = new Course(
                dto.getCode(),
                dto.getName(),
                dto.getDescription(),
                dto.getInstructorEmail(),
                dto.toModel().getStatus()
        );
        return repository.save(course);
    }

    public Course deactivateCourse(Course course) {
        course.setStatus(Status.INACTIVE);
        course.setInactivationAt(LocalDateTime.now());
        return repository.save(course);
    }
}
