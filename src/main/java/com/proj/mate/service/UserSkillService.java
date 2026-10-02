package com.proj.mate.service;

import com.proj.mate.dto.UserSkillRequestDto;
import com.proj.mate.dto.UserSkillResponseDto;
import com.proj.mate.entity.Skill;
import com.proj.mate.entity.UserInfo;
import com.proj.mate.entity.UserSkill;
import com.proj.mate.repository.SkillRepository;

import com.proj.mate.repository.UserRepository;
import com.proj.mate.repository.UserSkillRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserSkillService {

    private final UserSkillRepository userSkillRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public UserSkillService(UserSkillRepository userSkillRepository,
                            UserRepository userRepository,
                            SkillRepository skillRepository) {
        this.userSkillRepository = userSkillRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    private UserSkillResponseDto convertToDto(UserSkill userSkill) {
        return UserSkillResponseDto.builder()
                .id(userSkill.getId())
                .userId(userSkill.getUser().getId())
                .skill(userSkill.getSkill())
                .proficiency(userSkill.getProficiency())
                .build();
    }

    @Transactional
    public UserSkillResponseDto addUserSkill(UserSkillRequestDto requestDto) {
        UserInfo user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + requestDto.getUserId()));

        Skill skill;
        if (requestDto.getSkillId() != null) {
            skill = skillRepository.findById(requestDto.getSkillId())
                    .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + requestDto.getSkillId()));
        } else if (requestDto.getSkillName() != null && !requestDto.getSkillName().trim().isEmpty()) {
            String skillName = requestDto.getSkillName().trim();
            skill = skillRepository.findByNameIgnoreCase(skillName)
                    .orElseGet(() -> skillRepository.save(Skill.builder().name(skillName).build()));
        } else {
            throw new IllegalArgumentException("Either skillId or skillName must be provided");
        }

        Optional<UserSkill> existing = userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId());
        if (existing.isPresent()) {
            UserSkill userSkill = existing.get();
            userSkill.setProficiency(requestDto.getProficiency());
            return convertToDto(userSkillRepository.save(userSkill));
        }

        UserSkill userSkill = UserSkill.builder()
                .user(user)
                .skill(skill)
                .proficiency(requestDto.getProficiency())
                .build();

        return convertToDto(userSkillRepository.save(userSkill));
    }

    @Transactional(readOnly = true)
    public Optional<UserSkillResponseDto> getUserSkillById(Long id) {
        return userSkillRepository.findById(id).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<UserSkillResponseDto> getUserSkillsByUserId(Long userId) {
        return userSkillRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSkillResponseDto> getUsersBySkillId(Long skillId) {
        return userSkillRepository.findBySkillId(skillId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSkillResponseDto> getUsersBySkillAndMinProficiency(Long skillId, int minProficiency) {
        return userSkillRepository.findBySkillIdAndProficiencyGreaterThanEqual(skillId, minProficiency)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<UserSkillResponseDto> updateUserSkillProficiency(Long id, int proficiency) {
        return userSkillRepository.findById(id)
                .map(us -> {
                    us.setProficiency(proficiency);
                    return convertToDto(userSkillRepository.save(us));
                });
    }

    @Transactional
    public boolean deleteUserSkill(Long id) {
        if (!userSkillRepository.existsById(id)) {
            return false;
        }
        userSkillRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean deleteUserSkillByUserIdAndSkillId(Long userId, Long skillId) {
        Optional<UserSkill> userSkill = userSkillRepository.findByUserIdAndSkillId(userId, skillId);
        if (userSkill.isPresent()) {
            userSkillRepository.delete(userSkill.get());
            return true;
        }
        return false;
    }
}
