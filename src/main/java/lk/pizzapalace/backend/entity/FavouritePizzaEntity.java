package lk.pizzapalace.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class FavouritePizzaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pizzaId")
    private PizzaEntity pizzaEntity;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PizzaEntity getPizzaEntity() {
        return pizzaEntity;
    }

    public void setPizzaEntity(PizzaEntity pizzaEntity) {
        this.pizzaEntity = pizzaEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    

}
