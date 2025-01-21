package br.com.alura.ProjetoAlura.registration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByCourseCodeAndStudentEmail(String courseCode, String studentEmail);
}
