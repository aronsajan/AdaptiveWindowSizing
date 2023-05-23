package com.storageservice.filestorageservice.resourceassistant;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.storageservice.filestorageservice.StreamBuffer;

public class ResourceAssistant{

    int wakeInterval;
    int sampleCount;
    ActorSystem resMgmtSys;
    ActorRef monitorActor;
    public ResourceAssistant(int interval, int sampleCount, StreamBuffer streamBuffer){
        this.wakeInterval = interval;
        this.sampleCount = sampleCount;
        this.resMgmtSys = ActorSystem.create("ResourceManagementSystem");
        monitorActor = this.resMgmtSys.actorOf(WakeupActor.getProps(streamBuffer), "Monitor");
    }

    public void startMonitoring(){
        monitorActor.tell(new Messages.StartMonitoring(), Actor.noSender());
    }

    public void stopMonitoring(){
        try {
            monitorActor.tell(new Messages.StopMonitoring(), ActorRef.noSender());
            Thread.sleep(this.wakeInterval);
        } catch(InterruptedException interruptEx){
            System.out.println("Exception : "+interruptEx.getMessage());
        }
        this.resMgmtSys.stop(monitorActor);
    }
}
