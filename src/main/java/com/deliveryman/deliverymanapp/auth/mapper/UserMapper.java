package com.deliveryman.deliverymanapp.auth.mapper;

import com.deliveryman.deliverymanapp.auth.dto.UserResponse;
import com.deliveryman.deliverymanapp.auth.entity.LoginUser;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    UserResponse toResponse(LoginUser user);

    @AfterMapping
    default void populateRoles(LoginUser user, @MappingTarget UserResponse response) {
        Set<String> roles = new HashSet<>();
        if (Boolean.TRUE.equals(user.getIsAdmin())) {
            roles.add("ROLE_ADMIN");
        } else {
            roles.add("ROLE_DRIVER");
        }
        response.setRoles(roles);
    }
}
