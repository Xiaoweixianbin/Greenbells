
package dreammerwei.com.greenbellweather.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Drsg {

    @SerializedName("brf")
    @Expose
    private String brf;
    @SerializedName("txt")
    @Expose
    private String txt;

    /**
     * 
     * @return
     *     The brf
     */
    public String getBrf() {
        return brf;
    }

    /**
     * 
     * @param brf
     *     The brf
     */
    public void setBrf(String brf) {
        this.brf = brf;
    }

    /**
     * 
     * @return
     *     The txt
     */
    public String getTxt() {
        return txt;
    }

    /**
     * 
     * @param txt
     *     The txt
     */
    public void setTxt(String txt) {
        this.txt = txt;
    }

}
