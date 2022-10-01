package com.probodia.userservice.feign;

import com.probodia.userservice.api.vo.food.FoodInfoVO;
import com.probodia.userservice.api.vo.recordstat.RequestFoodDetailVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "food-service")
public interface FoodServiceClient {

    @PostMapping(value = "/api/detailFoodInfos", produces = "application/json")
    ResponseEntity<List<FoodInfoVO>> getFoodDetails(@RequestBody List<String> foodIds);


}
