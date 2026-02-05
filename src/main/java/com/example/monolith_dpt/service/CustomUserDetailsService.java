package com.example.monolith_dpt.service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Giả sử bạn lấy người dùng từ cơ sở dữ liệu
        // Đây chỉ là ví dụ, bạn cần thay thế bằng cách tìm người dùng thực tế
        if ("user".equals(username)) {
            return User.builder()
                    .username("user")
                    .password("{noop}password") // Sử dụng {noop} nếu không mã hóa mật khẩu trong ví dụ này
                    .roles("USER")
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
