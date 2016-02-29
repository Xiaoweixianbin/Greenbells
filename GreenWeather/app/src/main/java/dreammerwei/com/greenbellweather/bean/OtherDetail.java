package dreammerwei.com.greenbellweather.bean;

import java.util.List;

/**
 * Created by weixianbin on 16/2/29.
 */
public class OtherDetail {
    private String status;
    private OtherDetail2 data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OtherDetail2 getData() {
        return data;
    }

    public void setData(OtherDetail2 data) {
        this.data = data;
    }

    public class OtherDetail2{
        private List<Basic> basics;
    }
}
