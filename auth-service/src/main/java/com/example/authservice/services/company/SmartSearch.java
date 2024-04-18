/*
package com.example.authservice.services.company;
import org.apache.lucene.analysis.ru.RussianStemmer;

import java.util.ArrayList;
import java.util.List;

public class SmartSearch {
    public static List<String> smartSearch(String keyword, List<String> stringList) {
        List<String> filteredList = new ArrayList<>();
        RussianStemmer stemmer = new RussianStemmer();

        // Приводим ключевое слово к его основе
        stemmer.setCurrent(keyword);
        stemmer.stem();
        String stemmedKeyword = stemmer.getCurrent();

        for (String str : stringList) {
            // Приводим каждое слово в строке к его основе и ищем совпадения
            String[] words = str.split("\\s+");
            for (String word : words) {
                stemmer.setCurrent(word);
                stemmer.stem();
                String stemmedWord = stemmer.getCurrent();
                if (stemmedWord.equalsIgnoreCase(stemmedKeyword)) {
                    filteredList.add(str);
                    break;  // Если нашли хотя бы одно совпадение, прерываем внутренний цикл
                }
            }
        }

        return filteredList;
    }

    public static void main(String[] args) {
        String inputKeyword = "программисты";
        List<String> inputStringList = new ArrayList<>();
        inputStringList.add("Найти программиста");
        inputStringList.add("программисты на фрилансе");
        inputStringList.add("разработчики программного обеспечения");

        List<String> filteredStrings = smartSearch(inputKeyword, inputStringList);
        for (String str : filteredStrings) {
            System.out.println(str);
        }
    }
}

*/
