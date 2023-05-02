package youtube.java.puzzle.student.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "station_layouts")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "type_id")
    private int typeId;
    @Column(name = "station_id")
    private int stationId;
    @Column(name = "user_id")
    private int userId;
}