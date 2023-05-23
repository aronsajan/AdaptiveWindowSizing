package com.storageservice.filestorageservice.resourceassistant;

public class Messages {

    static class PredictWindowSize{}
    static class StartMonitoring{}
    static class StopMonitoring{}
    static class SetWindowSize{
        int windowSize;
        public SetWindowSize(int windowSize){
            this.windowSize = windowSize;
        }
    }


}
