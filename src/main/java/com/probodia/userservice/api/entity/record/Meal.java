package com.probodia.userservice.api.entity.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("MEAL")
@Table(name = "MEAL")
public class Meal extends Records {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_ID")
    private Long id;

    @Column(name = "TOTAL_CALORIES")
    private Integer totalCalories;

    @Column(name = "TOTAL_IMAGE_URL")
    private String totalImageUrl;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "MEAL_ID")
    @BatchSize(size = 100)
    private Set<MealDetail> mealDetails = new HashSet<>();

}
