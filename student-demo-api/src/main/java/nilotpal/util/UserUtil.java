package nilotpal.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import nilotpal.config.PropertyConfig;
import nilotpal.entity.User;
import nilotpal.service.UserService;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
        User user = Optional.ofNullable(cache.getIfPresent(userId)).orElse(userService.getUser(userId));
        if(null == user) {
            user = new User(userId, "not_found", "not_found", "not_found");
        }
        cache.put(userId, user);
        return user;
    }
}
