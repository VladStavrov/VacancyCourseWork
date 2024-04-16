package com.example.authservice.services.company;

import com.example.authservice.models.profile.Node;
import com.example.authservice.models.profile.Profile;
import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.models.vacancies.ExperienceLevel;
import com.example.authservice.models.vacancies.Vacancies;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecommendationEngine {
    public List<Vacancies> sortVacanciesByReckomended(Profile profile, List<WorkExperience> workExperiences, List<Vacancies> vacancies) {

        List<Map.Entry<Vacancies, Double>> vacancyMatchingList = new ArrayList<>();
        for (Vacancies vacancy : vacancies) {
            double vacancyMatchingPercentage = calculateVacancyMatchingPercentage(profile, workExperiences, vacancy);
            vacancyMatchingList.add(Map.entry(vacancy, vacancyMatchingPercentage));
        }
        vacancyMatchingList.sort(Comparator.<Map.Entry<Vacancies, Double>>comparingDouble(Map.Entry::getValue).reversed());
        List<Vacancies> sortedVacancies = vacancyMatchingList.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return sortedVacancies;
    }

    private double calculateVacancyMatchingPercentage(Profile profile, List<WorkExperience> workExperiences, Vacancies vacancy) {
        int experience = 0;
        switch (vacancy.getExperienceLevel().getDescription()) {
            case "Нет опыта":
                experience = 0;
                break;
            case "От 1 до 3 лет":
                experience = 12;
                break;
            case "От 3 до 5":
                experience = 36;
                break;
            case "От 5 лет и более":
                experience = 60;
                break;
            default:
                break;
        }
        //Посчитали общее количество месяцев нашего опыта
        int monthExpirience = calculateTotalExperienceInMonths(workExperiences);
        double experienceMatchingPercentage = 0;
        double skillsMatchingPercentage = 0;
        if (monthExpirience > 0 && monthExpirience < experience) {
            experienceMatchingPercentage = (double) monthExpirience / experience * 100 * 0.2;
        } else if (monthExpirience >= experience) {
            experienceMatchingPercentage = 0.2;
        }


            List<Map.Entry<Node, Double>> skillsMathingList = new ArrayList<>();
            for (Node node :
                    vacancy.getSkills()) {
                double nodeMatchingPercentage = calculateNodeMatchingPercentage(profile, workExperiences, node, experience, monthExpirience) * 0.8;
                skillsMathingList.add(Map.entry(node, nodeMatchingPercentage));
            }
            skillsMatchingPercentage = skillsMathingList.stream()
                    .mapToDouble(Map.Entry::getValue)
                    .average()
                    .orElse(0.0) * 0.8;

        return  skillsMatchingPercentage;


    }

    public int calculateTotalExperienceInMonths(List<WorkExperience> workExperiences) {
        int totalExperienceInMonths = 0;
        for (WorkExperience workExperience : workExperiences) {
            int experienceInMonths = calculateExperienceInMonths(workExperience.getDateStart(), workExperience.getDateFinish());
            totalExperienceInMonths += experienceInMonths;
        }
        return totalExperienceInMonths;
    }

    private int calculateExperienceInMonths(LocalDate startDate, LocalDate endDate) {
        Period period = Period.between(startDate, endDate);
        int months = (int) ChronoUnit.MONTHS.between(startDate, endDate);
        return months;
    }

    private double calculateNodeMatchingPercentage(Profile profile, List<WorkExperience> workExperiences, Node node,
                                                   int requiredExperience, int ourFullExperience) {

        int totalMonthExperience = 0;
        for (WorkExperience workExperience :
                workExperiences) {
            if (workExperience.getPrimarySkills().contains(node)) {
                int workExpMonth = calculateExperienceInMonths(workExperience.getDateStart(), workExperience.getDateFinish());
                totalMonthExperience += workExpMonth;

                break;
            }
            if (workExperience.getSecondarySkills().contains(node)) {
                int workExpMonth = calculateExperienceInMonths(workExperience.getDateStart(), workExperience.getDateFinish());
                totalMonthExperience += (int) (workExpMonth * 0.7);
                break;
            }
        }

        if(totalMonthExperience >= requiredExperience){
            return 100;
        } else  if (totalMonthExperience == 0 ){
            if (profile.getSkills().contains(node)) {
                if(ourFullExperience!=0){
                    return 100 * (0.2 * ourFullExperience) / requiredExperience;
                }
                else{
                    return 1;
                }

            } else {
                return 0;
            }
        }
        else{
             return (double) totalMonthExperience / requiredExperience;
        }



    }
}