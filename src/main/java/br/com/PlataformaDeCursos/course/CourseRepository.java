package br.com.PlataformaDeCursos.course;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
    boolean existsByCode(String code);
}