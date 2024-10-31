package com.eatda.login.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChildJoinRequest {
    private String childNumber;
    private String childName;
    private String childEmail;
    private String childPassword;
    private String childAddress;
}