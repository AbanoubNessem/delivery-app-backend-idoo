package com.deliveryman.deliverymanapp.auth.security;
import com.deliveryman.deliverymanapp.auth.entity.LoginUser;
import com.deliveryman.deliverymanapp.auth.repository.LoginUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginUserRepository userRepository;

    public CustomUserDetailsService(LoginUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUser user = userRepository.findByLoginName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login name: " + username));
        return new CustomUserDetails(user);
    }
}
