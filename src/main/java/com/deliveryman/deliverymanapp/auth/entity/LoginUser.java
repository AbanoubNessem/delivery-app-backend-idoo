package com.deliveryman.deliverymanapp.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_Gen_PermLogin")
@Getter
@Setter
public class LoginUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Person_Code")
    private String personCode;

    @Column(name = "LoginName", nullable = false, unique = true)
    private String loginName;

    @Column(name = "canLogin")
    private Boolean canLogin;

    @Column(name = "StartDate")
    private LocalDateTime startDate;

    @Column(name = "EndDate")
    private LocalDateTime endDate;

    @Column(name = "Notes")
    private String notes;

    @Column(name = "Package_Code")
    private String packageCode;

    @Column(name = "DAT")
    private String password;

    @Column(name = "isNodeRestricted")
    private Boolean isNodeRestricted;

    @Column(name = "Node_Code")
    private String nodeCode;

    @Column(name = "Branch_Code")
    private String branchCode;

    @Column(name = "isCompanyRestricted")
    private Boolean isCompanyRestricted;

    @Column(name = "Company_Code")
    private String companyCode;

    @Column(name = "isBranchRestricted")
    private Boolean isBranchRestricted;

    @Column(name = "isStoreRestricted")
    private Boolean isStoreRestricted;

    @Column(name = "isSBRestricted")
    private Boolean isSBRestricted;

    @Column(name = "Store_Code")
    private String storeCode;

    @Column(name = "SB_Code")
    private String sbCode;

    @Column(name = "LoginNameA")
    private String loginNameA;

    @Column(name = "isAdmin")
    private Boolean isAdmin;
}
