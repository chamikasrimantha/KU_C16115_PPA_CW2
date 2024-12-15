package lk.pizzapalace.backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity
public class Admin extends UserEntity {
    private String name;

    // relationships

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userentity", cascade = CascadeType.ALL)
    private List<PromotionEntity> promotionEntities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PromotionEntity> getPromotionEntities() {
        return promotionEntities;
    }

    public void setPromotionEntities(List<PromotionEntity> promotionEntities) {
        this.promotionEntities = promotionEntities;
    }

}
