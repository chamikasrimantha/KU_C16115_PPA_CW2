package lk.pizzapalace.backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lk.pizzapalace.backend.entity.enums.CheeseType;
import lk.pizzapalace.backend.entity.enums.CrustType;
import lk.pizzapalace.backend.entity.enums.SauceType;
import lk.pizzapalace.backend.entity.enums.ToppingsType;

@Entity
public class PizzaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pizzaId")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CrustType crustType;

    @Enumerated(EnumType.STRING)
    private SauceType sauceType;

    @Enumerated(EnumType.STRING)
    private ToppingsType toppingsType;

    @Enumerated(EnumType.STRING)
    private CheeseType cheeseType;

    private double price;

    // relationships

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pizzaEntity", cascade = CascadeType.ALL)
    private List<OrderEntity> orderEntities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CrustType getCrustType() {
        return crustType;
    }

    public void setCrustType(CrustType crustType) {
        this.crustType = crustType;
    }

    public SauceType getSauceType() {
        return sauceType;
    }

    public void setSauceType(SauceType sauceType) {
        this.sauceType = sauceType;
    }

    public ToppingsType getToppingsType() {
        return toppingsType;
    }

    public void setToppingsType(ToppingsType toppingsType) {
        this.toppingsType = toppingsType;
    }

    public CheeseType getCheeseType() {
        return cheeseType;
    }

    public void setCheeseType(CheeseType cheeseType) {
        this.cheeseType = cheeseType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

}
