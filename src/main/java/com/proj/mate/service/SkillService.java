package com.proj.mate.service;

import com.proj.mate.entity.Skill;
import com.proj.mate.repository.SkillRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    // CREATE
    public Skill createSkill(String skillName) {

        Skill newSkill = Skill.builder()
                .name(skillName)
                .build();

        return skillRepository.save(newSkill);
    }

    // GET BY ID
    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    // GET BY NAME
    public Optional<Skill> getSkillByName(String name) {
        return skillRepository.findByName(name);
    }

    // GET ALL
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    // SEARCH BY NAME - CASE INSENSITIVE
    public Optional<Skill> searchSkillsByName(String name) {
        return skillRepository.findByNameIgnoreCase(name);
    }

    // UPDATE
    public Optional<Skill> updateSkill(Long id, String updatedName) {

        return skillRepository.findById(id)
                .map(skill -> {
                    skill.setName(updatedName);
                    return skillRepository.save(skill);
                });
    }

    // DELETE
    public boolean deleteSkill(Long id) {

        if (!skillRepository.existsById(id)) {
            return false;
        }

        skillRepository.deleteById(id);
        return true;
    }


}