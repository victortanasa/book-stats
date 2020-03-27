package model;

import java.util.List;

public class ShelveMapping {

    private String normalizedValue;

    private List<String> alternateValues;

    public ShelveMapping() {
    }

    public ShelveMapping(final String normalizedValue, final List<String> alternateValues) {
        this.normalizedValue = normalizedValue;
        this.alternateValues = alternateValues;
    }

    public String getNormalizedValue() {
        return normalizedValue;
    }

    public List<String> getAlternateValues() {
        return alternateValues;
    }
}
