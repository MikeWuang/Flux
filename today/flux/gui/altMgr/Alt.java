package today.flux.gui.altMgr;

/**
 * Created by John on 2016/12/13.
 */
public class Alt {
    private String email;
    private String name;
    private String password;
    private boolean cracked;
    private boolean unchecked;
    private boolean starred;

    String message = "Null";

    public Alt(String email, String password, String name, String message) {
        this(email, password, name, false);
        this.message = message;
    }

    public Alt(String email, String password, String name) {
        this(email, password, name, false);
    }

    public Alt(String email, String password, String name, boolean starred) {
        this.email = email;
        this.starred = starred;

        if (password == null || password.isEmpty()) {
            cracked = true;
            unchecked = false;

            this.name = email;
            this.password = null;

        } else {
            cracked = false;
            unchecked = name == null || name.isEmpty();

            this.name = name;
            this.password = password;
        }
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getNameOrEmail() {
        return unchecked ? email : name;
    }

    public String getPassword() {
        if (password == null || password.isEmpty()) {
            cracked = true;
            return "";
        } else
            return password;
    }

    public boolean isCracked() {
        return cracked;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public boolean isUnchecked() {
        return unchecked;
    }

    public void setChecked(String name) {
        this.name = name;
        unchecked = false;
    }
}