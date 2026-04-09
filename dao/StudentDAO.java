package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Student;
import util.DBConnection;

public class StudentDAO {

    public StudentDAO() {
        try {
            initializeSchema();
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to initialize database schema: " + ex.getMessage(), ex);
        }
    }

    public void addStudent(Student s) throws SQLException {
        String sql = "INSERT INTO students(student_code, name, age, email, phone, department, course, academic_year, cgpa, status, city) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, s);
            ps.executeUpdate();
        }
    }

    public void updateStudent(Student s) throws SQLException {
        String sql = "UPDATE students SET student_code=?, name=?, age=?, email=?, phone=?, department=?, course=?, "
                + "academic_year=?, cgpa=?, status=?, city=? WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            fillStatement(ps, s);
            ps.setInt(12, s.getId());
            ps.executeUpdate();
        }
    }

    public ArrayList<Student> getAllStudents() throws SQLException {
        ArrayList<Student> list = new ArrayList<Student>();
        String sql = "SELECT id, student_code, name, age, email, phone, department, course, academic_year, cgpa, status, city, "
                + "DATE_FORMAT(created_at, '%d %b %Y %H:%i') AS created_label FROM students ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("student_code"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("department"),
                        rs.getString("course"),
                        rs.getString("academic_year"),
                        rs.getDouble("cgpa"),
                        rs.getString("status"),
                        rs.getString("city"),
                        rs.getString("created_label")
                );
                list.add(s);
            }
        }

        return list;
    }

    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private void fillStatement(PreparedStatement ps, Student s) throws SQLException {
        ps.setString(1, s.getStudentCode());
        ps.setString(2, s.getName());
        ps.setInt(3, s.getAge());
        ps.setString(4, s.getEmail());
        ps.setString(5, s.getPhone());
        ps.setString(6, s.getDepartment());
        ps.setString(7, s.getCourse());
        ps.setString(8, s.getAcademicYear());
        ps.setDouble(9, s.getCgpa());
        ps.setString(10, s.getStatus());
        ps.setString(11, s.getCity());
    }

    private void initializeSchema() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS students ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "student_code VARCHAR(40) UNIQUE, "
                    + "name VARCHAR(120) NOT NULL, "
                    + "age INT NOT NULL, "
                    + "email VARCHAR(120), "
                    + "phone VARCHAR(30), "
                    + "department VARCHAR(100), "
                    + "course VARCHAR(120) NOT NULL, "
                    + "academic_year VARCHAR(30), "
                    + "cgpa DECIMAL(3,2) DEFAULT 0.00, "
                    + "status VARCHAR(40) DEFAULT 'Active', "
                    + "city VARCHAR(80), "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")");

            ensureColumn(st, "ALTER TABLE students ADD COLUMN student_code VARCHAR(40) UNIQUE");
            ensureColumn(st, "ALTER TABLE students ADD COLUMN email VARCHAR(120)");
            ensureColumn(st, "ALTER TABLE students ADD COLUMN phone VARCHAR(30)");
            ensureColumn(st, "ALTER TABLE students ADD COLUMN department VARCHAR(100)");
            ensureColumn(st, "ALTER TABLE students ADD COLUMN academic_year VARCHAR(30)");
            ensureColumn(st, "ALTER TABLE students ADD COLUMN cgpa DECIMAL(3,2) DEFAULT 0.00");
            ensureColumn(st, "ALTER TABLE students ADD COLUMN status VARCHAR(40) DEFAULT 'Active'");
            ensureColumn(st, "ALTER TABLE students ADD COLUMN city VARCHAR(80)");
            ensureColumn(st, "ALTER TABLE students ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        }
    }

    private void ensureColumn(Statement st, String sql) throws SQLException {
        try {
            st.executeUpdate(sql);
        } catch (SQLException ex) {
            if (ex.getErrorCode() != 1060 && ex.getErrorCode() != 1061) {
                throw ex;
            }
        }
    }
}
