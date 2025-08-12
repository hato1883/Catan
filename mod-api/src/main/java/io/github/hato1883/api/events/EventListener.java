package io.github.hato1883.api.events;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventListener {
    // Optional event priority (default NORMAL)
    EventPriority priority() default EventPriority.NORMAL;
}
