package lk.pizzapalace.backend.service;

import lk.pizzapalace.backend.entity.Admin;
import lk.pizzapalace.backend.entity.Customer;
import lk.pizzapalace.backend.entity.UserEntity;

public interface UserService {
    // User Registration
    Customer registerCustomer(Customer customer);

    Admin registerAdmin(Admin admin);

    // User Login
    UserEntity login(UserEntity user);

    UserEntity getUserById(Long id);

}
