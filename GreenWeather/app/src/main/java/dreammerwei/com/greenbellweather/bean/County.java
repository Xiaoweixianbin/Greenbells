package dreammerwei.com.greenbellweather.bean;

/**
 * Created by Administrator on 2015/7/26.
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;


    public int getId() {
        return id;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }


    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
