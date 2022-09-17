package com.probodia.userservice.api.entity.record;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("MEAL_DETAIL")
@Table(name = "MEAL_DETAIL")
public class MealDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_DETAIL_ID")
    private Long id;

    @Column(name = "CALORIE")
    private Integer calorie;

    @Column(name = "IMAGE_URL")
    @Size(max = 100)
    private String imageUrl;

    @Column(name = "BLOOD_SUGAR")
    private Integer bloodSugar;

    @Column(name = "FOOD_NAME")
    @NotNull
    private String foodName;

    @Column(name = "QUANTITY")
    @NotNull
    private Integer quantity;

    @Column(name = "FOOD_ID")
    private String foodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEAL_ID", insertable = false,
    updatable = false)
    @JsonIgnore
    private Meal meal;

}
