package com.deliveryman.deliverymanapp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String personCode;
    private String loginName;
    private String branchCode;
    private String companyCode;
    private String storeCode;
    private String nodeCode;
    private Boolean isAdmin;
    private Set<String> roles;
}
