package day21;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import client.ReadDataClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    private void execute() {
        List<String> data = ReadDataClient.getInputDataList("day21.txt");

        Pattern pattern = Pattern.compile("^(.+) \\(contains (.+)\\)$");
        for (String line : data) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                List<String> ingredients = Arrays.asList(matcher.group(1).trim().split(" "));
                List<String> allergens = Arrays.asList(matcher.group(2).trim().split(","))
                                               .stream()
                                               .map(l -> l.trim())
                                               .collect(Collectors.toList());

                setupIngredientsMap(ingredients);
                setupCandidatesMap(ingredients, allergens);
            }
        }
        Map<String, String> fixedCandidatesMap = setupFixedCandidatesMap();
        log.info("Part1={}", countIngredientsNotAllergens(fixedCandidatesMap));
        log.info("Part2={}", getIngredientsNotAllergensStr(fixedCandidatesMap));
    }

    private String getIngredientsNotAllergensStr(Map<String, String> fixedCandidatesMap){
        List<String> allergens = fixedCandidatesMap.keySet()
                                                   .stream()
                                                   .sorted()
                                                   .collect(Collectors.toList());
        List<String> ingredients = new ArrayList<>();
        for(String allergen: allergens) {
            ingredients.add(fixedCandidatesMap.get(allergen));
        }
        return String.join(",", ingredients);
    }

    Map<String, List<String>> candidatesMap = new HashMap<>();
    Map<String, Integer> ingredientsMap = new HashMap<>();

    private int countIngredientsNotAllergens(Map<String, String> fixedCandidatesMap){
        int cnt = 0;
        for (Map.Entry<String, Integer> entry : ingredientsMap.entrySet()) {
            if(!fixedCandidatesMap.containsValue(entry.getKey())){
                cnt += entry.getValue();
            }
        }
        return cnt;
    }

    private void setupIngredientsMap(List<String> ingredients) {
        for (String ingredient : ingredients) {
            if (ingredientsMap.containsKey(ingredient)) {
                int cnt = ingredientsMap.get(ingredient);
                ingredientsMap.put(ingredient, cnt + 1);
            } else {
                ingredientsMap.put(ingredient, 1);
            }
        }
    }

    private Map<String, String> setupFixedCandidatesMap() {
        Map<String, String> fixedCandidatesMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : candidatesMap.entrySet()) {
            if (entry.getValue().size() == 1) {
                fixedCandidatesMap.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        for (int i = 0; i < 5; i++) {
            for (Map.Entry<String, List<String>> entry : candidatesMap.entrySet()) {
                List<String> candidate = new ArrayList<>(entry.getValue());
                if (entry.getValue().size() > 1) {
                    for (Map.Entry<String, String> entry2 : fixedCandidatesMap.entrySet()) {
                        if (candidate.contains(entry2.getValue())) {
                            candidate.remove(entry2.getValue());
                        }
                    }
                }
                candidatesMap.put(entry.getKey(), candidate);
            }
            for (Map.Entry<String, List<String>> entry : candidatesMap.entrySet()) {
                if (entry.getValue().size() == 1) {
                    fixedCandidatesMap.put(entry.getKey(), entry.getValue().get(0));
                }
            }
        }
        return fixedCandidatesMap;
    }

    private void setupCandidatesMap(List<String> ingredients, List<String> allergens) {
        for (String allergen : allergens) {
            if (candidatesMap.containsKey(allergen)) {
                List<String> candidates = candidatesMap.get(allergen);

                List<String> newCandidates = new ArrayList();
                for (String candidate : candidates) {
                    if (ingredients.contains(candidate)) {
                        newCandidates.add(candidate);
                    }
                }
                candidatesMap.put(allergen, newCandidates);
            } else {
                candidatesMap.put(allergen, ingredients);
            }
        }
    }
}
