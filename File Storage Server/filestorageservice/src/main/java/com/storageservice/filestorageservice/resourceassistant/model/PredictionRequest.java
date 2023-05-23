package com.learn.azureuploader.resourceassistant.model;

import java.io.Serializable;
import java.util.List;

public class PredictionRequest  implements Serializable {
    List<Integer> dataRates;

    public List<Integer> getDataRates() {
        return dataRates;
    }

    public void setDataRates(List<Integer> dataRates) {
        this.dataRates = dataRates;
    }

}
