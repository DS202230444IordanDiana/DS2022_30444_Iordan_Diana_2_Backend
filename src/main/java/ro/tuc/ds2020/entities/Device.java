package ro.tuc.ds2020.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.entities.users.Person;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @SequenceGenerator(name = "seq",
            sequenceName = "seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Long maxLimit;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Person owner;

    @OneToMany(mappedBy = "device", cascade = CascadeType.REMOVE)
    private List<Measurement> measurements;

    public Device(String type, String model, Person owner) {
        this.type = type;
        this.model = model;
        this.owner = owner;
    }

    public Device(String type, String model, Long limit) {
        this.type = type;
        this.model = model;
        this.maxLimit = limit;
    }
}
