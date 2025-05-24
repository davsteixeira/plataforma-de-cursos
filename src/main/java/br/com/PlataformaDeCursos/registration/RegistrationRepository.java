package br.com.PlataformaDeCursos.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByCourseCodeAndStudentEmail(String courseCode, String studentEmail);

    @Query(value = """
    SELECT 
        c.name AS courseName, 
        c.code AS courseCode, 
        u.name AS instructorName, 
        u.email AS instructorEmail, 
        COUNT(r.studentEmail) AS totalRegistrations
    FROM Registration r
    JOIN Course c ON r.courseCode = c.code
    JOIN User u ON c.instructorEmail = u.email
    WHERE c.status = 'ACTIVE'
    GROUP BY c.name, c.code, u.name, u.email
    ORDER BY COUNT(r.studentEmail) DESC
    LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getCourseRegistrationReportRaw(); // Eu optei por retornar apenas os 5 primeiros cursos e que estejam ativos no relatório

}