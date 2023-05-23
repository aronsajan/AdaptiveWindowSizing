package com.storageservice.filestorageservice.resourceassistant;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.storageservice.filestorageservice.StreamBuffer;

public class WakeupActor extends AbstractActor {


    boolean monitoringFlag;
    ActorRef resPredictor;
    StreamBuffer streamBuffer;
    public WakeupActor(StreamBuffer streamBuffer){
        this.monitoringFlag = false;
        this.streamBuffer = streamBuffer;
    }

    static Props getProps(StreamBuffer streamBuffer){
        return Props.create(WakeupActor.class, streamBuffer);
    }

    @Override
    public Receive createReceive() {
        return this.receiveBuilder().match(Messages.StartMonitoring.class, (args)->{
            this.resPredictor = this.getContext().actorOf(ResourcePredictionActor.getProps(this.streamBuffer));
            this.monitoringFlag = true;
            while(this.monitoringFlag){
                Thread.sleep(3000);
                resPredictor.tell(new Messages.PredictWindowSize(), this.getSelf());
            }
            this.sender().tell("Completed", this.getSelf());
        }).match(Messages.StartMonitoring.class, (args)->{
            this.monitoringFlag = false;
        }).build();
    }
}
