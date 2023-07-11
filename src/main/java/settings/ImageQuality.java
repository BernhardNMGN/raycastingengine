package settings;

public enum ImageQuality {



    HIGH(1.), MEDIUM(.5), LOW(.25), ULTRA_LOW(.1);

    private double scalingFactor;

    private ImageQuality(double scalingFactor) {
        this.scalingFactor =scalingFactor;
    }

    public double getScalingFactor() {
        return this.scalingFactor;
    }
}
