package ro.tuc.ds2020.entities.users;

import javax.persistence.Entity;

@Entity
public class Admin extends User{

    public Admin() {
        this.setUserType(UserType.ADMIN);
    }
}
