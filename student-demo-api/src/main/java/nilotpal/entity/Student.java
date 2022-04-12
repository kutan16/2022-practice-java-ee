package nilotpal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Student {
    @JsonProperty
    private Integer student_id;
    @JsonProperty
    private String student_name;
    @JsonProperty
    private String student_major;

    public Student() {
    }

    public Student(Integer student_id, String student_name, String student_major) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.student_major = student_major;
    }

    public Student(Student student) {
        this.student_id = student.student_id;
        this.student_name = student.student_name;
        this.student_major = student.student_major;
    }

//    @JsonIgnore
//    public Integer getStudent_id() {
//        return student_id;
//    }
//
//    @JsonIgnore
//    public void setStudent_id(Integer student_id) {
//        this.student_id = student_id;
//    }
//
//    @JsonIgnore
//    public String getStudent_name() {
//        return student_name;
//    }
//
//    @JsonIgnore
//    public void setStudent_name(String student_name) {
//        this.student_name = student_name;
//    }
//
//    @JsonIgnore
//    public String getStudent_major() {
//        return student_major;
//    }
//
//    @JsonIgnore
//    public void setStudent_major(String student_major) {
//        this.student_major = student_major;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Student student = (Student) o;
//        return Objects.equals(student_id, student.student_id) && Objects.equals(student_name, student.student_name) && Objects.equals(student_major, student.student_major);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(student_id, student_name, student_major);
//    }
//
//    @Override
//    public String toString() {
//        return String.format("Student \n{\n   student_id : %s,\n   student_name : %s,\n   student_major : %s\n}", student_id, student_name, student_major);
//    }
}
