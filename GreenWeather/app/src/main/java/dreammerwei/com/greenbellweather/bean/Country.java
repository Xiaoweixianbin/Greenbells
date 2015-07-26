package dreammerwei.com.greenbellweather.bean;

/**
 * Created by Administrator on 2015/7/26.
 */
public class Country {
    private int id;
    private String conuntryName;
    private String conuntryCode;
    private int cityId;


    public int getId() {
        return id;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }


    public String getConuntryCode() {
        return conuntryCode;
    }

    public void setConuntryCode(String conuntryCode) {
        this.conuntryCode = conuntryCode;
    }

    public String getConuntryName() {
        return conuntryName;
    }

    public void setConuntryName(String conuntryName) {
        this.conuntryName = conuntryName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
