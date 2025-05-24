package br.com.PlataformaDeCursos.course;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;

@Entity(name="Course")
@Table(name = "Course")
public class Course {

    @Id
    @Column(length = 10)
    private String code;

    private String name;

    private String description;

    @Email
    private String instructorEmail;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime inactivationAt;

    public Course() {}

    public Course(String code, String name, String description, String instructorEmail, Status status) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.instructorEmail = instructorEmail;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getInactivationAt() {
        return inactivationAt;
    }

    public void setInactivationAt(LocalDateTime inactivationAt) {
        this.inactivationAt = inactivationAt;
    }
}