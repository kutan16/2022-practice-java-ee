package nilotpal.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import nilotpal.config.PropertyConfig;
import nilotpal.entity.User;
import nilotpal.service.UserService;

import java.util.Optional;
import java.util.concurrent.*;

/**
 * Util class to process user id to fetch User
 */
public class UserUtil {
    static PropertyConfig properties = PropertyConfig.getInstance();
    /**
     * Cache Builder to set up cache
     */
    private static final Cache<String, User> cache = Caffeine.newBuilder()
            .expireAfterWrite(Long.parseLong(properties.get("cache.expiryAfterWrite")), TimeUnit.SECONDS)
            .expireAfterAccess(Long.parseLong(properties.get("cache.expiryAfterAccess")), TimeUnit.SECONDS)
            .maximumSize(Long.parseLong(properties.get("cache.maxSize")))
            .initialCapacity(Integer.parseInt(properties.get("cache.initialLoad")))
            .build();

    /**
     * Process the userId of every single request and return User
     *
     * @param userService the user service injection forwarded from {@link nilotpal.jaxrs.resource.StudentResource}
     * @param userId      the userId of the user
     * @return the User
     */
    public static User processUserId(UserService userService, String userId) {
        User user = cache.getIfPresent(userId);
        if(null == user) {
            try {
                Thread.sleep(50);
                System.out.println("slept");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            user = userService.getUser(userId);
            if(null != user) {
                cache.put(userId, user);
            }
        }
        return user;
    }

    private static User executeUser(String userId, UserService userService) {
        User user = null;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            user = userService.getUser(userId);
            if(null != user) {
                cache.put(userId, user);
            }
        return user;
    }
}
