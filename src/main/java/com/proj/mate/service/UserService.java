package com.proj.mate.service;

import com.proj.mate.dto.UserRequestDto;
import com.proj.mate.dto.UserResponseDto;
import com.proj.mate.entity.UserInfo;
import com.proj.mate.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + requestDto.getEmail());
        }

        UserInfo user = modelMapper.map(requestDto, UserInfo.class);
        user.setId(null);

        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("USER");
        }

        UserInfo savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto updatedUserDto) {
        UserInfo existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (updatedUserDto.getName() != null) {
            existingUser.setName(updatedUserDto.getName());
        }
        if (updatedUserDto.getEmail() != null) {
            existingUser.setEmail(updatedUserDto.getEmail());
        }
        if (updatedUserDto.getAbout() != null) {
            existingUser.setAbout(updatedUserDto.getAbout());
        }
        if (updatedUserDto.getProfilePic() != null) {
            existingUser.setProfilePic(updatedUserDto.getProfilePic());
        }
        if (updatedUserDto.getGithubLink() != null) {
            existingUser.setGithubLink(updatedUserDto.getGithubLink());
        }
        if (updatedUserDto.getLinkedinLink() != null) {
            existingUser.setLinkedinLink(updatedUserDto.getLinkedinLink());
        }

        if (updatedUserDto.getPassword() != null && !updatedUserDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        }

        if (updatedUserDto.getRole() != null && !updatedUserDto.getRole().trim().isEmpty()) {
            existingUser.setRole(updatedUserDto.getRole());
        }

        UserInfo savedUser = userRepository.save(existingUser);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Transactional
    public UserResponseDto updateProfilePic(Long id, String profilePicUrl) {
        UserInfo existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setProfilePic(profilePicUrl);
        UserInfo savedUser = userRepository.save(existingUser);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByRole(String role) {
        return userRepository.findByRole(role)
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByRoleIn(List<String> roles) {
        return userRepository.findByRoleIn(roles)
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByRoleNotIn(List<String> roles) {
        return userRepository.findByRoleNotIn(roles)
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByNameContainingIgnoreCase(String name) {
        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }
}
