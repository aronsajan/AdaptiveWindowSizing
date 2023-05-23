package com.storageservice.filestorageservice.resourceassistant;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import com.learn.azureuploader.resourceassistant.model.PredictionRequest;
import com.learn.azureuploader.resourceassistant.model.PredictionResponse;
import com.storageservice.filestorageservice.StreamBuffer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class ResourcePredictionActor extends AbstractActor {

    StreamBuffer streamBuffer;
    List<Integer> dataRates;
    int prevDataOffset;
    ActorRef resAdjustmentActor;
    public ResourcePredictionActor(StreamBuffer streamBuffer){
        this.streamBuffer = streamBuffer;
        this.dataRates = new ArrayList<>();
        this.prevDataOffset = 0;
        this.resAdjustmentActor = this.getContext().actorOf(ResourceAdjustmentActor.getProps(this.streamBuffer));
    }

    static Props getProps(StreamBuffer streamBuffer){
        return Props.create(ResourcePredictionActor.class, streamBuffer);
    }
    @Override
    public Receive createReceive() {
        return new ReceiveBuilder().match(Messages.PredictWindowSize.class, (args)->{
            try{
                int totalDataRead = this.streamBuffer.getTotalDataRead();
                int dataRate = totalDataRead - this.prevDataOffset;
                this.prevDataOffset = this.streamBuffer.getTotalDataRead();
                dataRates.add(dataRate);
                if(dataRates.size()==4) {
                    PredictionRequest requestModel = new PredictionRequest();
                    requestModel.setDataRates(dataRates);
                    System.out.println(dataRates);
                    MultiValueMap<String, String> headerMap= new LinkedMultiValueMap<>();
                    headerMap.add("Content-Type", "application/json");
                    HttpEntity<PredictionRequest> request = new HttpEntity<>(requestModel, headerMap);
                    ResponseEntity<PredictionResponse> predictionResponse = new RestTemplate().exchange("http://127.0.0.1:5000/windowsize",
                            HttpMethod.POST,
                            request,
                            PredictionResponse.class);
                    int predictedWindowSize = predictionResponse.getBody().getPredictedWindowSize();
                    System.out.println("Predicted Window Size : "+predictedWindowSize);
                    this.resAdjustmentActor.tell(new Messages.SetWindowSize(predictedWindowSize), this.getSelf());
                    dataRates.clear();
                }
            } catch(Exception ex){
                System.out.println("Exception - "+ex.getMessage());
                dataRates.clear();
            }
        }).build();
    }
}
