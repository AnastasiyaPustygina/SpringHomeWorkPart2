package org.example.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class LibraryAvailabilityHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);
        return minutes % 2 == 0 ? Health.up().build() :
                Health.down().status(Status.DOWN).build();
    }
}
