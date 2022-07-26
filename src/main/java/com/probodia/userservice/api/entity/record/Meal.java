package com.probodia.userservice.api.entity.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("MEAL")
@Table(name = "MEAL")
public class Meal extends Record{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_ID")
    private Long id;

    @Column(name = "TOTAL_CALORIES")
    private Integer totalCalories;

    @Column(name = "TOTAL_IMAGE_URL")
    private String totalImageUrl;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "MEAL_ID")
    private List<MealDetail> mealDetails = new ArrayList<>();

}
