package nilotpal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Student {
//    @JsonProperty
    private Integer student_id;
//    @JsonProperty
    private String student_name;
//    @JsonProperty
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
}
