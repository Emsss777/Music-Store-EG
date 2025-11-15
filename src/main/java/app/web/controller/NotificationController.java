package app.web.controller;

import app.mapper.UserMapper;
import app.model.entity.User;
import app.model.enums.NotificationStatus;
import app.notification.client.dto.NotificationDTO;
import app.notification.client.dto.NotificationPreferenceDTO;
import app.notification.services.NotificationService;
import app.security.AuthenticationMetadata;
import app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

import static app.util.ModelAttributes.*;
import static app.util.Redirects.REDIRECT_NOTIFICATIONS;
import static app.util.UrlPaths.*;
import static app.util.Views.VIEW_NOTIFICATIONS;

@Controller
@RequiredArgsConstructor
@RequestMapping(URL_NOTIFICATIONS)
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping
    public ModelAndView getNotificationPage(@AuthenticationPrincipal AuthenticationMetadata authMetadata) {

        User currentUser = userService.getUserById(authMetadata.getUserId());

        NotificationPreferenceDTO notificationPrefDTO =
                notificationService.getNotificationPreference(currentUser.getId());

        List<NotificationDTO> notificationHistory = notificationService.getNotificationHistory(currentUser.getId());

        long succeededNotificationsNumber = notificationHistory.stream()
                .filter(notification -> notification.getStatus().equals(NotificationStatus.SUCCEEDED.name()))
                .count();

        long failedNotificationsNumber = notificationHistory.stream()
                .filter(notification -> notification.getStatus().equals(NotificationStatus.FAILED.name()))
                .count();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_NOTIFICATIONS);
        modelAndView.addObject(MODEL_USER, UserMapper.toBasicDTO(currentUser));
        modelAndView.addObject(MODEL_NOTIFICATIONS_PREFERENCE, notificationPrefDTO);
        modelAndView.addObject(MODEL_NOTIFICATION_HISTORY, notificationHistory);
        modelAndView.addObject(MODEL_SUCCEEDED_NOTIFICATIONS_NUMBER, succeededNotificationsNumber);
        modelAndView.addObject(MODEL_FAILED_NOTIFICATIONS_NUMBER, failedNotificationsNumber);

        return modelAndView;
    }

    @PutMapping(URL_USER_PREFERENCE)
    public ModelAndView updateUserPreference(@RequestParam(name = "enabled") boolean enabled,
                                             @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        notificationService.updateNotificationPreference(authenticationMetadata.getUserId(), enabled);

        return new ModelAndView(REDIRECT_NOTIFICATIONS);
    }

    @DeleteMapping
    public ModelAndView clearUserHistory(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        UUID userId = authenticationMetadata.getUserId();

        notificationService.clearHistory(userId);

        return new ModelAndView(REDIRECT_NOTIFICATIONS);
    }

    @DeleteMapping(URL_CLEAR)
    public ModelAndView clearAllNotifications() {

        notificationService.clearAllHistory();
        return new ModelAndView(REDIRECT_NOTIFICATIONS);
    }

    @PutMapping
    public ModelAndView retryFailedNotifications(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        UUID userId = authenticationMetadata.getUserId();

        notificationService.retryFailed(userId);

        return new ModelAndView(REDIRECT_NOTIFICATIONS);
    }
}
