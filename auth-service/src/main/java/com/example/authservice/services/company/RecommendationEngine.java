package com.example.authservice.services.company;
import com.example.authservice.DTOs.company.vacancy.VacancyRecommendedDTO;
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
    private static final double EXPERIENCE_WEIGHT = 0.3;
    private static final double SKILLS_WEIGHT = 1 - EXPERIENCE_WEIGHT;
    private static final int MAX_EXPERIENCE_DIFFERENCE = 30;
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
    public List<VacancyRecommendedDTO> getRecommendedData(Profile profile, List<WorkExperience> workExperiences, List<Vacancies> vacancies){
        List<Map.Entry<Vacancies, Double>  > vacancyMatchingList = new ArrayList<>();
        for (Vacancies vacancy : vacancies) {
            double vacancyMatchingPercentage = calculateVacancyMatchingPercentage(profile, workExperiences, vacancy);

            vacancyMatchingList.add(Map.entry(vacancy, vacancyMatchingPercentage));
        }
        vacancyMatchingList.sort(Comparator.<Map.Entry<Vacancies, Double>>comparingDouble(Map.Entry::getValue).reversed());
        List<VacancyRecommendedDTO> recommendedDTOList = new ArrayList<>();

        for (Map.Entry<Vacancies, Double> entry : vacancyMatchingList) {
            Vacancies vacancy = entry.getKey();
            Double matchingPercentage =  entry.getValue();
            int roundedMatchingPercentage = (int) Math.ceil(matchingPercentage);
            VacancyRecommendedDTO recommendedDTO = new VacancyRecommendedDTO(vacancy, roundedMatchingPercentage);
            recommendedDTOList.add(recommendedDTO);
        }
        return recommendedDTOList;

    }
    private double calculateVacancyMatchingPercentage(Profile profile, List<WorkExperience> workExperiences, Vacancies vacancy) {
        int requiredExperience = getRequiredExperienceInMonths(vacancy.getExperienceLevel());
        double ourFullExperience = calculateTotalExperienceInMonths(workExperiences);
        double experienceMatchingPercentage = calculateExperienceMatchingPercentage(requiredExperience, ourFullExperience);
        double skillsMatchingPercentage = calculateSkillsMatchingPercentage(profile, workExperiences, vacancy) * 100;

        double experienceDifference = Math.abs(requiredExperience - ourFullExperience);
        double experienceRecommendationFactor = 1.0;

        if (vacancy.getExperienceLevel() != ExperienceLevel.SENIOR) {
            experienceRecommendationFactor = 1.0 - (experienceDifference / MAX_EXPERIENCE_DIFFERENCE);
            if (experienceRecommendationFactor < 0) {
                experienceRecommendationFactor = 0;
            }
        }
        return (experienceMatchingPercentage * EXPERIENCE_WEIGHT * experienceRecommendationFactor) +
                (skillsMatchingPercentage * SKILLS_WEIGHT);

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
    private double calculateTotalExperienceInMonths(List<WorkExperience> workExperiences) {
        return workExperiences.stream()
                .mapToDouble(we -> calculateExperienceInMonths(we.getDateStart(), we.getDateFinish()))
                .sum();
    }
    private double calculateExperienceInMonths(LocalDate startDate, LocalDate endDate) {
        if(endDate == null){
            endDate = LocalDate.now();
        }
        return (double) ChronoUnit.MONTHS.between(startDate, endDate);
    }
    private double calculateExperienceMatchingPercentage(int requiredExperience, double ourFullExperience) {
        if (ourFullExperience >= requiredExperience) {
            return 100.0;
        } else {
            return  ourFullExperience / requiredExperience * 100;
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
        double totalMonthExperience = workExperiences.stream()
                .filter(we -> we.getPrimarySkills().contains(node) || we.getSecondarySkills().contains(node))
                .mapToDouble(we -> {
                    double experience = calculateExperienceInMonths(we.getDateStart(), we.getDateFinish());
                    return we.getSecondarySkills().contains(node) ? experience * 0.7 : experience;
                })
                .sum();

        if(requiredExperience==0){
            if (totalMonthExperience == 0){
                return profile.getSkills().contains(node) ? 1 : 0.0;
            }
            else{
                return 1;
            }
        }
        if (totalMonthExperience == 0) {
            return profile.getSkills().contains(node) ? 0.02 : 0.0;
        } else {
            return Math.min(1.0,  totalMonthExperience /requiredExperience);
        }
    }
}