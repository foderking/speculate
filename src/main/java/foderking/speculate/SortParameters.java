package foderking.speculate;

public enum SortParameters {
    RATING("Rating"),
    BATTERY("Battery Runtime"),
    BRIGHTNESS("Brightness"),
    COVERAGE_ADOBERGB("Adobe RGB Coverage"),
    COVERAGE_P3("P3 Coverage"),
    COVERAGE_SRGB("sRGB Coverage"),
    MAX_TEMPERATURE_IDLE("Max Temp Idle"),
    MAX_TEMPERATURE_LOAD("Max Temp Load"),
    RESPONSE_BW("Response Time, Black-White"),
    RESPONSE_GG("Response Time, Gray-Gray"),
    WEIGHT("Weight"),
    THICKNESS("Thickness");

    private String value;

    private SortParameters(String value){
	this.value = value;
    }
    
    public String getValue(){
	return this.value;
    }
}
