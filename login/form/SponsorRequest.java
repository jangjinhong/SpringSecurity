package com.eatda.login.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SponsorRequest {
    private String sponsorName;
    private String sponsorEmail;
    private String sponsorPassword;
    private String sponsorAddress;
}