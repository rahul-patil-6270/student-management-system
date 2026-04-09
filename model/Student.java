package model;

class Person {
    protected String name;
    protected int age;
}

public class Student extends Person {
    private int id;
    private String studentCode;
    private String email;
    private String phone;
    private String department;
    private String course;
    private String academicYear;
    private double cgpa;
    private String status;
    private String city;
    private String createdAt;

    public Student() {
    }

    public Student(int id, String studentCode, String name, int age, String email, String phone,
                   String department, String course, String academicYear, double cgpa,
                   String status, String city, String createdAt) {
        this.id = id;
        this.studentCode = studentCode;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.course = course;
        this.academicYear = academicYear;
        this.cgpa = cgpa;
        this.status = status;
        this.city = city;
        this.createdAt = createdAt;
    }

    public String display() {
        return studentCode + " - " + name + " - " + course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
