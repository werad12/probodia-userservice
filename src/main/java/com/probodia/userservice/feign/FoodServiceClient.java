package com.probodia.userservice.feign;

import com.probodia.userservice.api.dto.food.FoodInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "food-service")
public interface FoodServiceClient {

    @PostMapping(value = "/api/detailFoodInfos", produces = "application/json")
    ResponseEntity<List<FoodInfoDto>> getFoodDetails(@RequestBody List<String> foodIds);


}
