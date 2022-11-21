package ro.tuc.ds2020.entities.users;

import lombok.Getter;
import lombok.Setter;
import ro.tuc.ds2020.entities.Device;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class Person extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String name;

    @Column( nullable = false)
    private String address;

    @Column( nullable = false)
    private int age;

    @Column( nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<Device> devices;

    public Person() {
    }

    public Person(String username, String name, String address, int age, String password, UserType userType) {
        super.setUsername(username);
        this.name = name;
        this.address = address;
        this.age = age;
        this.password = password;
        this.setUserType(userType);
    }


    public UserType getUserType(){
        return super.getUserType();
    }
}
