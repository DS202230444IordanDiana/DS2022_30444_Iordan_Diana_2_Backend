package ro.tuc.ds2020.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.tuc.ds2020.dtos.validators.annotation.AgeLimit;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class PersonDetailsDTO {


    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String address;
    @AgeLimit(limit = 18)
    private int age;
    @NotNull
    private String password;

    public PersonDetailsDTO(Long id, String username, String name, String address, int age) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public PersonDetailsDTO( String name, String address, int age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public PersonDetailsDTO(Long id, String name, String address, int age) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDetailsDTO that = (PersonDetailsDTO) o;
        return age == that.age &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, age);
    }
}
