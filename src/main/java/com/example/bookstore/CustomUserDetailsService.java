package com.example.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

        System.out.println("★ ログイン試行: " + username);
        System.out.println("★ DB取得成功: " + user.getUsername());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                java.util.Collections.singletonList(
                        new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole())
                )
        );
    }
}