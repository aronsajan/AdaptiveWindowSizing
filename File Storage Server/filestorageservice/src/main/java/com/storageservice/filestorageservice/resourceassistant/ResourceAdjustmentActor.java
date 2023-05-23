package com.storageservice.filestorageservice.resourceassistant;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.storageservice.filestorageservice.StreamBuffer;

public class ResourceAdjustmentActor extends AbstractActor {

    StreamBuffer streamBuffer;

    static Props getProps(StreamBuffer streamBuffer){
        return Props.create(ResourceAdjustmentActor.class, streamBuffer);
    }

    public ResourceAdjustmentActor(StreamBuffer streamBuffer){
        this.streamBuffer = streamBuffer;
    }

    @Override
    public Receive createReceive() {
        return this.receiveBuilder().match(Messages.SetWindowSize.class, (arg)->{
            this.streamBuffer.updateBufferSize(arg.windowSize);
        }).build();
    }
}
