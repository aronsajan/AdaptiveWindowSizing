package com.learn.azureuploader.resourceassistant.model;

import java.io.Serializable;

public class PredictionResponse implements Serializable {
    int predictedWindowSize;

    public int getPredictedWindowSize() {
        return predictedWindowSize;
    }

    public void setPredictedWindowSize(int predictedWindowSize) {
        this.predictedWindowSize = predictedWindowSize;
    }
}
