package com.example.app5.controller.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = false)
@Builder
@Data
public class DeveloperReq {
    @NotNull
    @Size(min = 3, max = 30)
    private final String userName;

    @Email(regexp = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")
    @NotNull
    @Size(min = 3, max = 50)
    private final String mail;

    public static DeveloperReq newInstance() {
        return DeveloperReq.builder().build();
    }
}
