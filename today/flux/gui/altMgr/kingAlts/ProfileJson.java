package today.flux.gui.altMgr.kingAlts;

import com.google.gson.annotations.SerializedName;

public class ProfileJson {
    @SerializedName("username")
    String username;
    @SerializedName("generated")
    int generated;
    @SerializedName("generatedToday")
    int generatedToday;
    @SerializedName("message")
    String message;
}