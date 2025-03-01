package com.my_geeks.geeks.domain.user.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class NotifyAllow {
    private boolean serviceNotify;
    private boolean chatNotify;
    private boolean roommateNotify;
    private boolean marketingNotify;
}
