package br.com.alura.ProjetoAlura.registration;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incrementa
    private Long id;  // Criei um ID único da matrícula para facilitar buscas no banco de dados, pensando a longo prazo

    @Column(nullable = false, length = 10)
    private String courseCode;

    @Column(nullable = false, length = 50)
    private String studentEmail;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    public Registration() {}

    public Registration(String courseCode, String studentEmail) {
        this.courseCode = courseCode;
        this.studentEmail = studentEmail;
        this.registrationDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
}

