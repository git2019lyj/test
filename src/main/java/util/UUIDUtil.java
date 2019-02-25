package util;

import java.util.UUID;

/**
 * @author liuyuejie
 * @create 2018-12-26 15:11
 */
public class UUIDUtil {
    public static String get32UUId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
