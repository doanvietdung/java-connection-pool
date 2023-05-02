package youtube.java.puzzle.college.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bags_packages")
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "package_order")
    private int packageOrder;
    @Column(name = "bag_order")
    private int bagOrder;
    @Column(name = "cuid")
    private int cuid;
}