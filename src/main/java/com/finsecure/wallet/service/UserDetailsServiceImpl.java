package com.finsecure.wallet.service;

import java.util.ArrayList;
import java.util.List;

import com.finsecure.wallet.model.LoggedInUser;
import com.finsecure.wallet.model.User;
import com.finsecure.wallet.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Transactional(
            readOnly = true
    )
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUserName(username);
        if (user != null) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList();
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getPrimaryRole().getRoleCode()));
            LoggedInUser liu = new LoggedInUser(username, user.getPassword(), true, true, true, true, grantedAuthorities, user.getPrimaryRole(), user);
            return liu;
        } else {
            logger.debug("No user found with user name -> " + username);
            throw new SessionAuthenticationException("Please check your credentials. Either Username or Password is wrong");
        }
    }
}
