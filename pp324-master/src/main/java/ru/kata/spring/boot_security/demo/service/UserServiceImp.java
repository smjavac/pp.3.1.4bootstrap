package ru.kata.spring.boot_security.demo.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import java.util.List;


@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImp(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", email));
        }
        return user;
    }

    @Transactional
    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public List<User> showUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.getById(id);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


    @Transactional
    @Override
    public void update(User user) {
        User updatedUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getPassword() != null && !user.getPassword().equals(updatedUser.getPassword())) {
            updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName((user.getLastName()));
        updatedUser.setAge(user.getAge());
        updatedUser.setEmail(user.getEmail());
        if (user.getRoles() == null) {
            updatedUser.setRoles(userRepository.getById(user.getId()).getRoles());
        }
        userRepository.save(updatedUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }
}