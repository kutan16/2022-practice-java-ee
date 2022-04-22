package nilotpal.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import nilotpal.entity.User;
import nilotpal.service.UserService;

import java.util.concurrent.TimeUnit;

/**
 * Util class to process user id to fetch User
 */
public class UserUtil {
    /**
     * Cache Builder to set up cache
     */
    private static final Cache<String, User> cache = Caffeine
            .newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .maximumSize(20)
            .initialCapacity(5)
            .build();

    /**
     * Process the userId of every single request and return back User
     *
     * @param userService the userservice injection forwarded from {@link nilotpal.jaxrs.resource.StudentResource}
     * @param userId      the userId of the user
     * @return the User
     */
    public static User processUserId(UserService userService, String userId) {
        User user = cache.getIfPresent(userId);
        if(null == user) {
            user = userService.getUser(userId);
            if(null == user) {
                user = new User(userId, "not_found", "not_found", "not_found");
            }
            cache.put(userId, user);
        }
        return user;
    }
}
