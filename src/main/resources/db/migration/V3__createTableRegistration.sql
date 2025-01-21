CREATE TABLE Registration (
    id BIGINT NOT NULL AUTO_INCREMENT,  -- ID único para a matrícula
    courseCode VARCHAR(10) NOT NULL,
    studentEmail VARCHAR(50) NOT NULL,
    registrationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY unique_course_student (courseCode, studentEmail)  -- Garante que a combinação é única
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

