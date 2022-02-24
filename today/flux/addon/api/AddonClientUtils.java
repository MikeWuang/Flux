package today.flux.addon.api;

import com.soterdev.SoterObfuscator;
import today.flux.Flux;
import today.flux.addon.api.utils.Image;

/**
 * 客户端信息获取
 */
public class AddonClientUtils {
    static AddonClientUtils INSTANCE = new AddonClientUtils();

    /**
     * 获取客户端Utils实例
     *
     * @return Utils实例
     */
    public static AddonClientUtils getInstance() {
        return INSTANCE;
    }

    /**
     * 获取用户UID
     *
     * @return 用户UID
     */
    public int getUID() {
        return 0;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        return "DEV";
    }

    /**
     * 获取用户头像Base64
     *
     * @return 用户头像Base64
     */
    public Image getUserAvatar() {
        return null;
    }

    /**
     * 获取客户端版本号
     *
     * @return 客户端版本号
     */
    public float getClientVersion() {
        return Flux.VERSION;
    }
}
