package com.project.projectLoginInExtensionsApi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtensionLoginRequest {
    private String username;
    private String password;
    private Integer extensionNumber;
}
