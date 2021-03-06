package com.lambdaschool.cookbook.services;


import com.lambdaschool.cookbook.exceptions.ResourceNotFoundException;
import com.lambdaschool.cookbook.models.User;
import com.lambdaschool.cookbook.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service(value = "securityUserService")
public class SecurityUserServiceImpl
        implements UserDetailsService {

    /**
     * Ties this implementation to the User Repository so we can find a user in the database.
     */
    @Autowired
    private UserRepo userRepo;

    /**
     * Verifies that the user is correct and if so creates the authenticated user
     *
     * @param s The user name we are look for
     * @return a security user detail that is now an authenticated user
     * @throws ResourceNotFoundException if the user name is not found
     */
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String s)
            throws ResourceNotFoundException {
        User user = userRepo.findByUsername(s.toLowerCase());
        if (user == null) {
            throw new ResourceNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.getAuthority()
        );
    }

}