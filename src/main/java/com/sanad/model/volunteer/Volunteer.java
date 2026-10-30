package com.sanad.model.volunteer;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * MODEL: a volunteer with skills, available days and logged hours.
 */
public class Volunteer {
    private int id;
    private Integer userId;
    private String username;
    private String name;
    private String skills;
    private String availableDays;
    private BigDecimal totalHours;

    /**
     * {@code skills} and {@code availableDays} are comma-separated, e.g. "TEACHING,FIRST_AID".
     */
    public Volunteer(int id, Integer userId, String username, String name, String skills, String availableDays,
                     BigDecimal totalHours) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.skills = skills;
        this.availableDays = availableDays;
        this.totalHours = totalHours;
    }

    public int getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSkills() {
        return skills;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public List<String> skillNames() {
        return split(skills);
    }

    public Set<DayOfWeek> days() {
        Set<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
        split(availableDays).forEach(d -> days.add(DayOfWeek.valueOf(d)));
        return days;
    }

    public boolean hasSkill(String skill) {
        return skillNames().contains(skill);
    }

    public boolean isAvailableOn(LocalDate date) {
        return days().contains(date.getDayOfWeek());
    }

    private static List<String> split(String csv) {
        return csv == null || csv.isBlank() ? List.of() : Arrays.asList(csv.split(","));
    }
}
