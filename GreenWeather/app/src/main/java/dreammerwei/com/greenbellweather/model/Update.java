
package dreammerwei.com.greenbellweather.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Update {

    @SerializedName("loc")
    @Expose
    private String loc;
    @SerializedName("utc")
    @Expose
    private String utc;

    /**
     * 
     * @return
     *     The loc
     */
    public String getLoc() {
        return loc;
    }

    /**
     * 
     * @param loc
     *     The loc
     */
    public void setLoc(String loc) {
        this.loc = loc;
    }

    /**
     * 
     * @return
     *     The utc
     */
    public String getUtc() {
        return utc;
    }

    /**
     * 
     * @param utc
     *     The utc
     */
    public void setUtc(String utc) {
        this.utc = utc;
    }

}
