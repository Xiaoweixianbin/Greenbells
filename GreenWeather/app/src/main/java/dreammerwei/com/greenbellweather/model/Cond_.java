
package dreammerwei.com.greenbellweather.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cond_ {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("txt")
    @Expose
    private String txt;

    /**
     * 
     * @return
     *     The code
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    public void setCode(String code) {
        this.code = code;
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
