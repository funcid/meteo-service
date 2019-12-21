/**
 * @author func 21.12.2019
 */
public enum  Sensor {

    SVIBLOVO("55.856336, 37.654575", ""),
    CENTER("55.753126, 37.620839", ""),
    ;

    private String location;
    private String lastData;

    Sensor(String location, String lastData) {
        this.location = location;
        this.lastData = lastData;
    }

    public String getLocation() {
        return location;
    }

    public String getLastData() {
        return lastData;
    }

    public void setLastData(String lastData) {
        this.lastData = lastData;
    }
}
