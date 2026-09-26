package com.proj.mate.dto;


import lombok.Data;

@Data
public class SkillRequest {

    private String name;

    public SkillRequest() {
    }

    public SkillRequest(String name) {
        this.name = name;
    }



}