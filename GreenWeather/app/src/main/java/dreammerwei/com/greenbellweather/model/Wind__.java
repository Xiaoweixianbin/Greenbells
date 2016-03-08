
package dreammerwei.com.greenbellweather.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wind__ implements Serializable {

    @SerializedName("deg")
    @Expose
    private String deg;
    @SerializedName("dir")
    @Expose
    private String dir;
    @SerializedName("sc")
    @Expose
    private String sc;
    @SerializedName("spd")
    @Expose
    private String spd;

    /**
     * 
     * @return
     *     The deg
     */
    public String getDeg() {
        return deg;
    }

    /**
     * 
     * @param deg
     *     The deg
     */
    public void setDeg(String deg) {
        this.deg = deg;
    }

    /**
     * 
     * @return
     *     The dir
     */
    public String getDir() {
        return dir;
    }

    /**
     * 
     * @param dir
     *     The dir
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * 
     * @return
     *     The sc
     */
    public String getSc() {
        return sc;
    }

    /**
     * 
     * @param sc
     *     The sc
     */
    public void setSc(String sc) {
        this.sc = sc;
    }

    /**
     * 
     * @return
     *     The spd
     */
    public String getSpd() {
        return spd;
    }

    /**
     * 
     * @param spd
     *     The spd
     */
    public void setSpd(String spd) {
        this.spd = spd;
    }

}
