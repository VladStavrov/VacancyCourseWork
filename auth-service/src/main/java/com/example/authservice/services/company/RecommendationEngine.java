package com.example.authservice.services.company;

import com.example.authservice.models.profile.Node;
import com.example.authservice.models.profile.Profile;
import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.models.vacancies.ExperienceLevel;
import com.example.authservice.models.vacancies.Vacancies;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class RecommendationEngine {
    private static final double EXPERIENCE_WEIGHT = 0.2;
    private static final double SKILLS_WEIGHT = 0.8;

    public List<Vacancies> sortVacanciesByRecommended(Profile profile, List<WorkExperience> workExperiences, List<Vacancies> vacancies) {
        List<Map.Entry<Vacancies, Double>  > vacancyMatchingList = new ArrayList<>();
        for (Vacancies vacancy : vacancies) {
            double vacancyMatchingPercentage = calculateVacancyMatchingPercentage(profile, workExperiences, vacancy);
            vacancyMatchingList.add(Map.entry(vacancy, vacancyMatchingPercentage));
        }
        vacancyMatchingList.sort(Comparator.<Map.Entry<Vacancies, Double>>comparingDouble(Map.Entry::getValue).reversed());
        return vacancyMatchingList.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double calculateVacancyMatchingPercentage(Profile profile, List<WorkExperience> workExperiences, Vacancies vacancy) {
        int requiredExperience = getRequiredExperienceInMonths(vacancy.getExperienceLevel());
        int ourFullExperience = calculateTotalExperienceInMonths(workExperiences);
        double experienceMatchingPercentage = calculateExperienceMatchingPercentage(requiredExperience, ourFullExperience);

        double skillsMatchingPercentage = calculateSkillsMatchingPercentage(profile, workExperiences, vacancy);

        return experienceMatchingPercentage * EXPERIENCE_WEIGHT + skillsMatchingPercentage * SKILLS_WEIGHT;
    }

    private int getRequiredExperienceInMonths(ExperienceLevel experienceLevel) {
        switch (experienceLevel) {
            case NO_EXPERIENCE:
                return 0;
            case JUNIOR:
                return 12;
            case MIDDLE:
                return 36;
            case SENIOR:
                return 60;
            default:
                return 0;
        }
    }
    private int calculateTotalExperienceInMonths(List<WorkExperience> workExperiences) {
        return workExperiences.stream()
                .mapToInt(we -> calculateExperienceInMonths(we.getDateStart(), we.getDateFinish()))
                .sum();
    }
    private int calculateExperienceInMonths(LocalDate startDate, LocalDate endDate) {
        if(endDate == null){
            endDate = LocalDate.now();
        }
        return (int) ChronoUnit.MONTHS.between(startDate, endDate);
    }
    private double calculateExperienceMatchingPercentage(int requiredExperience, int ourFullExperience) {
        if (ourFullExperience >= requiredExperience) {
            return 100.0;
        } else {
            return (double) ourFullExperience / requiredExperience * 100;
        }
    }
    private double calculateSkillsMatchingPercentage(Profile profile, List<WorkExperience> workExperiences, Vacancies vacancy) {
        List<Map.Entry<Node, Double>> skillsMatchingList = new ArrayList<>();
        for (Node node : vacancy.getSkills()) {
            int requiredExperience = getRequiredExperienceInMonths(vacancy.getExperienceLevel());
            double nodeMatchingPercentage = calculateNodeMatchingPercentage(profile, workExperiences, node,requiredExperience);
            skillsMatchingList.add(Map.entry(node, nodeMatchingPercentage));
        }
        return skillsMatchingList.stream()
                .mapToDouble(Map.Entry::getValue)
                .average()
                .orElse(0.0);
    }
    private double calculateNodeMatchingPercentage(Profile profile, List<WorkExperience> workExperiences, Node node,int requiredExperience) {
        int totalMonthExperience = workExperiences.stream()
                .filter(we -> we.getPrimarySkills().contains(node) || we.getSecondarySkills().contains(node))
                .mapToInt(we -> calculateExperienceInMonths(we.getDateStart(), we.getDateFinish()))
                .sum();

        if (totalMonthExperience == 0) {
            return profile.getSkills().contains(node) ? 1.0 : 0.0;
        } else {
            return Math.min(1.0, (double) totalMonthExperience /requiredExperience);
        }
    }
}