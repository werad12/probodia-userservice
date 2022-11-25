package com.probodia.userservice.api.service.record;

import com.probodia.userservice.api.entity.record.*;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.MealDetailRepository;
import com.probodia.userservice.api.repository.record.RecordRepository;
import com.probodia.userservice.api.dto.food.FoodInfoDto;
import com.probodia.userservice.api.dto.medicine.MedicineStatisticDetailDto;
import com.probodia.userservice.api.dto.recordstat.AverageNeutrientDto;
import com.probodia.userservice.api.dto.recordstat.MedicineStatDto;
import com.probodia.userservice.api.dto.recordstat.RangeBSugarDto;
import com.probodia.userservice.api.dto.recordstat.RecordPercentDto;
import com.probodia.userservice.config.properties.AppProperties;
import com.probodia.userservice.feign.FoodServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordStatisticService {

    private final RecordRepository recordRepository;
    private final AppProperties appProperties;
    private final MealDetailRepository mealDetailRepository;
    private final FoodServiceClient foodServiceClient;


    public RangeBSugarDto getBSugarRange(User user, String stdate, String endate) {

//        log.debug("LOW : {}, HIGH : {}",appProperties.getBloodSugar().getLow(), appProperties.getBloodSugar().getHigh());


        log.debug("start : {}, end : {}",stdate,endate);
        List<Records> records = recordRepository.findAllByUserAndRecordDateBetweenAndType(user, stdate, endate,"SUGAR");

        int total = 0;
        AtomicInteger low = new AtomicInteger();
        AtomicInteger mid = new AtomicInteger();
        AtomicInteger high = new AtomicInteger();

        records.stream().forEach(r -> {
            BSugar sugar = (BSugar) r;
            Integer bsugar = sugar.getBloodSugar();
            String timeTag = r.getTimeTag().getValue();

            if(timeTag.contains("식전")){
                if(bsugar<appProperties.getBloodSugar().getBeforeLow()){
                    low.getAndIncrement();
                }
                else if(bsugar>appProperties.getBloodSugar().getBeforeHigh()){
                    high.getAndIncrement();
                }
                else{
                    mid.getAndIncrement();
                }
            }
            //식후
            else{
                if(bsugar<appProperties.getBloodSugar().getAfterLow()){
                    low.getAndIncrement();
                }
                else if(bsugar>appProperties.getBloodSugar().getAfterHigh()){
                    high.getAndIncrement();
                }
                else{
                    mid.getAndIncrement();
                }
            }
            log.debug("SUGAR : {}, date : {}, timetag : {}",sugar.getBloodSugar(),r.getRecordDate(),timeTag);
        });

        total = low.get() + mid.get() + high.get();
        return new RangeBSugarDto(total,low.get(),mid.get(),high.get());
    }

    @Transactional
    public AverageNeutrientDto getAverageNutrient(User user, String stdate, String endate) {

        //데이터 가져온다.
        List<Records> records = recordRepository.findAllByUserAndRecordDateBetweenAndType(user, stdate, endate,"MEAL");
        List<Meal> mealList = records.stream().map(r -> (Meal) r).collect(Collectors.toList());

        List<MealDetail> mealDetails = mealDetailRepository.findAllByMealIn(mealList);

//        mealDetails.forEach(m -> log.debug("MEAL : {}",m.getFoodName()));

        //foodId값으로 음식 정보 가져온다. Map으로 가져온다.
        List<String> foodIds = mealDetails.stream().map(MealDetail::getFoodId).collect(Collectors.toList());
        ResponseEntity<List<FoodInfoDto>> response = foodServiceClient.getFoodDetails(foodIds);

        List<FoodInfoDto> foodDetails = response.getBody();
        //일일별로 더하고, 컬럼 개수만큼 나눈다.
        Map<String, Vector<Double>> proteinMap = new HashMap<>();
        Map<String, Vector<Double>> carboMap = new HashMap<>();
        Map<String, Vector<Double>> fatMap = new HashMap<>();

        mealDetails.stream().forEach(m -> {
            Meal meal = m.getMeal();
            String date = meal.getRecordDate().substring(0,10);
            log.debug("DATE : {}",date);
            String foodName = m.getFoodName();
            FoodInfoDto foodInfo = foodDetails.stream().filter(e -> e.getName().equals(foodName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Not found food with foodId"));

            Vector<Double> p = proteinMap.get(date);
            if(p==null) p = new Vector<>();
            p.add(foodInfo.getProtein());

            Vector<Double> c = carboMap.get(date);
            if(c==null) c = new Vector<>();
            c.add(foodInfo.getCarbohydrate());

            Vector<Double> f = fatMap.get(date);
            if(f==null) f = new Vector<>();
            f.add(foodInfo.getFat());

            proteinMap.put(date,p);
            carboMap.put(date,c);
            fatMap.put(date,f);
        });

        Integer dayCnt = proteinMap.size();
        Double protein = getDayAvg(proteinMap);
        Double carbo = getDayAvg(carboMap);
        Double fat = getDayAvg(fatMap);

        return new AverageNeutrientDto(protein/dayCnt, carbo/dayCnt, fat/dayCnt);
    }

    private Double getDayAvg(Map<String,Vector<Double>> map){
        Double ret = 0.0;

        for(String key : map.keySet()){
//            log.debug(" VALUE : {}",proteinMap.get(key).get(0));
            Double dayAvg = 0.0;
            int daycnt = 0;
            for(Double d : map.get(key)){
                dayAvg+= d;
                daycnt++;
            }
            dayAvg /= daycnt;
            ret += dayAvg;
        }

        return ret;
    }


    public RecordPercentDto getRecordPercent(User user, String stdate, String endate) throws ParseException {

        List<Records> records = recordRepository.findAllByUserAndRecordDateBetween(user, stdate, endate);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date st = formatter.parse(stdate);
        Date en = formatter.parse(endate);

        long diff = (en.getTime() - st.getTime()) /(1000 * 24 * 60 * 60) + 1;
        diff *= 4;
        log.debug("st : {}, en : {}, diff : {}, sz : {}",st,en,diff,records.size());

        AtomicInteger morningCnt = new AtomicInteger();
        AtomicInteger noonCnt = new AtomicInteger();
        AtomicInteger nightCnt = new AtomicInteger();

        AtomicReference<String> prev = new AtomicReference<>("");

        records.stream().forEach(r -> {
            log.debug("IDX : {}",r.getId());
            if(r.getType().equals("SUGAR")){
                log.debug("TYPE : {}",r.getTimeTag().getValue());

                if (r.getTimeTag().getValue().equals("아침 식전")) {
                    if(!prev.equals(r.getRecordDate().substring(0,10))){
                        morningCnt.getAndIncrement();
                        prev.set(r.getRecordDate().substring(0, 10));
                        log.debug("PREV : {}",prev);
                    }
                }
                else if(r.getTimeTag().getValue().equals("아침 식후")){
                    if(!prev.equals(r.getRecordDate().substring(0,10))){
                        morningCnt.getAndIncrement();
                        prev.set(r.getRecordDate().substring(0, 10));
                    }
                }
                else if (r.getTimeTag().getValue().equals("점심 식전")) {
                    if(!prev.equals(r.getRecordDate().substring(0,10))){
                        noonCnt.getAndIncrement();
                        prev.set(r.getRecordDate().substring(0, 10));
                    }
                }
                else if(r.getTimeTag().getValue().equals("점심 식후")){
                    if(!prev.equals(r.getRecordDate().substring(0,10))){
                        noonCnt.getAndIncrement();
                        prev.set(r.getRecordDate().substring(0, 10));
                    }
                }
                else if (r.getTimeTag().getValue().equals("저녁 식전")) {
                    if(!prev.equals(r.getRecordDate().substring(0,10))){
                        nightCnt.getAndIncrement();
                        prev.set(r.getRecordDate().substring(0, 10));
                    }
                }
                else if(r.getTimeTag().getValue().equals("저녁 식후")){
                    if(!prev.equals(r.getRecordDate().substring(0,10))){
                        nightCnt.getAndIncrement();
                        prev.set(r.getRecordDate().substring(0, 10));
                    }
                }

            }
            else{
                if (r.getTimeTag().getValue().equals("아침")) {
                    morningCnt.getAndIncrement();
                }
                else if(r.getTimeTag().getValue().equals("점심")){
                    noonCnt.getAndIncrement();
                }
                else{
                    nightCnt.getAndIncrement();
                }
            }
        });

        return new RecordPercentDto(morningCnt.get() * 100/(int)diff, noonCnt.get() * 100/(int)diff, nightCnt.get() * 100/(int)diff);
    }


    @Transactional
    public MedicineStatDto getMedicineStat(User user, String stdate, String endate) {

        List<Records> medicine = recordRepository.findAllByUserAndRecordDateBetweenAndType(user, stdate, endate, "MEDICINE");

        List<MedicineStatisticDetailDto> ret = new ArrayList<>();
        List<String> set = new ArrayList<>();

        for(Records r : medicine){

            Medicine m = (Medicine) r;
            Set<MedicineDetail> medicineDetails = m.getMedicineDetails();
            for(MedicineDetail md : medicineDetails){
                set.add(md.getMedicineName());
            }
        }

        set = set.stream().distinct().collect(Collectors.toList());
        for(String name : set){
            ret.add(new MedicineStatisticDetailDto(name, 0));
        }


        for(Records r : medicine){

            Medicine m = (Medicine) r;

            Set<MedicineDetail> medicineDetails = m.getMedicineDetails();

            for(MedicineDetail md : medicineDetails){
                for(MedicineStatisticDetailDto mvo : ret){
                    if(mvo.getMedicineName().equals(md.getMedicineName())){
                        mvo.setMedicineCnt(mvo.getMedicineCnt() + 1);
                    }
                }
            }

        }

        return new MedicineStatDto(ret);
    }

    public Double getHemoglobin(User user, String stdate, String endate) {

        List<Records> records = recordRepository.findAllByUserAndRecordDateBetweenAndType(user, stdate, endate, "SUGAR");

        Double ret = 0.0;
        Double avg = 0.0;

        for(Records r : records){
            BSugar bs = (BSugar) r;
            avg+= bs.getBloodSugar();
        }

        avg /= records.size();

        log.info("AVG : {}",avg);

        ret = (avg + 46.7)/28.7;

        return  ret;
    }
}
