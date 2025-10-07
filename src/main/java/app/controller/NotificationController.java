package app.controller;

import app.model.entity.UserEntity;
import app.model.enums.NotificationStatus;
import app.notification.client.dto.Notification;
import app.notification.client.dto.NotificationPreference;
import app.notification.services.NotificationService;
import app.security.AuthenticationMetadata;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static app.util.ModelAttributes.*;
import static app.util.UrlPaths.URL_NOTIFICATIONS;
import static app.util.Views.VIEW_NOTIFICATIONS;

@Controller
@RequestMapping(URL_NOTIFICATIONS)
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(UserService userService, NotificationService notificationService) {

        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ModelAndView getNotificationPage(@AuthenticationPrincipal AuthenticationMetadata authMetadata) {

        UserEntity currentUser = userService.getUserById(authMetadata.getUserId());

        NotificationPreference notificationPreference =
                notificationService.getNotificationPreference(currentUser.getId());

        List<Notification> notificationHistory = notificationService.getNotificationHistory(currentUser.getId());

        long succeededNotificationsNumber = notificationHistory.stream()
                .filter(notification ->
                        NotificationStatus.fromString(notification.getStatus()) == NotificationStatus.SUCCEEDED)
                .count();

        long failedNotificationsNumber = notificationHistory.stream()
                .filter(notification ->
                        NotificationStatus.fromString(notification.getStatus()) == NotificationStatus.FAILED)
                .count();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_NOTIFICATIONS);
        modelAndView.addObject(MODEL_USER, currentUser);
        modelAndView.addObject(MODEL_NOTIFICATIONS_PREFERENCE, notificationPreference);
        modelAndView.addObject(MODEL_NOTIFICATION_HISTORY, notificationHistory);
        modelAndView.addObject(MODEL_SUCCEEDED_NOTIFICATIONS_NUMBER, succeededNotificationsNumber);
        modelAndView.addObject(MODEL_FAILED_NOTIFICATIONS_NUMBER, failedNotificationsNumber);

        return modelAndView;
    }
}
