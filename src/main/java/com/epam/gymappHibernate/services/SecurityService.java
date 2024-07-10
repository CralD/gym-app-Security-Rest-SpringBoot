package com.epam.gymappHibernate.services;
import com.epam.gymappHibernate.dao.UserRepository;
import com.epam.gymappHibernate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

    @Service
    public class SecurityService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = userRepository.getUserByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    new ArrayList<GrantedAuthority>()
            );
        }
    }