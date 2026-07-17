package com.namiq.msuser.scheduler;

import com.namiq.msuser.dao.entity.User;
import com.namiq.msuser.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserScheduler {
    private final UserRepository userRepository;



    @Scheduled(cron = "0 0 0 * * MON")
    public void logInactiveUsersOlderThan90Days() {
        LocalDateTime date=LocalDateTime.now().minusDays(90);
        List<User> users = userRepository.findInactiveUsers(date);
        users.forEach(user ->
                log.info("Inactive user: {} ({}) last login: {}",
                        user.getUsername(),
                        user.getEmail(),
                        user.getLastLoginTime()));


    }

}
