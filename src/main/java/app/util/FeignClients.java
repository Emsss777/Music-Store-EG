package app.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FeignClients {

    public static final String NOTIFICATION_SVC = "notification-svc";
    public static final String NOTIFICATION_SVC_URL = "${notification-svc.base-url}";
}
