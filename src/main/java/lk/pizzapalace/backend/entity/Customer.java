package lk.pizzapalace.backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity
public class Customer extends UserEntity {
    private List<PizzaEntity> favourites;
    private int loyaltyPoints;
    private String phone;
    private String address;
    private String name;

    // relationships

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userentity", cascade = CascadeType.ALL)
    private List<OrderEntity> orderEntities;

    public List<PizzaEntity> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<PizzaEntity> favourites) {
        this.favourites = favourites;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

}