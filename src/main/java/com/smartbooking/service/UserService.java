package com.smartbooking.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartbooking.dto.UserRequest;
import com.smartbooking.dto.UserResponse;
import com.smartbooking.entity.User;
import com.smartbooking.exception.UserNotFoundException;
import com.smartbooking.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Service //This class should be managed by me
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
    }
private UserResponse mapToUserResponse(User user) {

    UserResponse response = new UserResponse();

    response.setId(user.getId());
    response.setFirstName(user.getFirstName());
    response.setLastName(user.getLastName());
    response.setEmail(user.getEmail());
    response.setPhone(user.getPhone());
    response.setRole(user.getRole());

    return response;
}
    public UserResponse createUser(UserRequest request) {
        User user = new User();

    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setPhone(request.getPhone());
    user.setRole(request.getRole());

    User savedUser = userRepository.save(user);
    UserResponse response = new UserResponse();

    response.setId(savedUser.getId());
    response.setFirstName(savedUser.getFirstName());
    response.setLastName(savedUser.getLastName());
    response.setEmail(savedUser.getEmail());
    response.setPhone(savedUser.getPhone());
    response.setRole(savedUser.getRole());

    return response;
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return mapToUserResponse(user);
    }

    public Page<UserResponse> getAllUsers(Pageable pageable){
        Page<User> users = userRepository.findAll(pageable);

        return users.map(this::mapToUserResponse);
    }

    public UserResponse updateUser(Long id,UserRequest request){
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User not found with id : "+id));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());

        User updateUser = userRepository.save(user);

        return mapToUserResponse((updateUser));
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id
                        ));

        userRepository.delete(user);
    }
}
