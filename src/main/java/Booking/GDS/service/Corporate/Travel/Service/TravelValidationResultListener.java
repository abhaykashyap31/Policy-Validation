package Booking.GDS.service.Corporate.Travel.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import Booking.GDS.service.Corporate.Travel.Dto.TravelValidationResultEvent;
import Booking.GDS.service.Corporate.Travel.Entities.Travel;
import Booking.GDS.service.Corporate.Travel.Repo.TravelRepository;
import lombok.extern.slf4j.Slf4j;

@Service 
@Slf4j 
public class TravelValidationResultListener {

    @Autowired 
    private TravelRepository travelRepository;

    // @KafkaListener(topics = "${kafka.topic.travel-result}", groupId = "${spring.kafka.consumer.group-id}")
    // public void handleValidationResult(TravelValidationResultEvent result) {
        
    //     log.info("Received validation outcome for travel ID {}: {}", result.travelId(), result.status());

    //     Optional<Travel> t = travelRepository.findById(result.travelId());

    //     if(!t.isPresent()){
    //         log.warn("Cannot find travel with ID: {}", result.travelId());
    //         return;
    //     }

    //     Travel trv = t.get();
    //     trv.setTravelStatus(result.status());

    //     travelRepository.save(trv);
    // }

    private final ObjectMapper objectMapper;

    TravelValidationResultListener(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = "${kafka.topic.travel-result}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleValidationResult(String rawJson){

        try {
            TravelValidationResultEvent result = objectMapper.readValue(rawJson, TravelValidationResultEvent.class);
            log.info("Received validation outcome for travel ID {}: {}", result.travelId(), result.status());

            Optional<Travel> t = travelRepository.findById(result.travelId());

            if(!t.isPresent()){
                log.warn("Cannot find travel with ID: {}", result.travelId());
                return;
            }

            Travel trv = t.get();
            trv.setTravelStatus(result.status());

            travelRepository.save(trv);

        } catch (Exception e) {
            // TODO: handle exception
            log.error("Failed to parse event JSON: {}", rawJson, e);
        }

    }





}
