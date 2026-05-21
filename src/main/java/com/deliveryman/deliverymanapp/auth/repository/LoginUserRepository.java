package com.deliveryman.deliverymanapp.auth.repository;

import com.deliveryman.deliverymanapp.auth.entity.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoginUserRepository extends JpaRepository<LoginUser, Long> {
    Optional<LoginUser> findByLoginName(String loginName);
}
