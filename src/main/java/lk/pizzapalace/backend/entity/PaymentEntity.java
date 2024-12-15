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
import lk.pizzapalace.backend.entity.enums.PaymentType;

@Entity
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentId")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private double price;

    private boolean isLoyaltyUsed;

    // relationships

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paymentEntity", cascade = CascadeType.ALL)
    private List<OrderEntity> orderEntities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isLoyaltyUsed() {
        return isLoyaltyUsed;
    }

    public void setLoyaltyUsed(boolean isLoyaltyUsed) {
        this.isLoyaltyUsed = isLoyaltyUsed;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

}
