package com.kltn.individualservice.util.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.individualservice.entity.StudyClass;

import java.util.*;

public class RegisterGraphColoringUtil {

    public static class ClassSchedule {
        public String code;
        public String name;
        public String dayOfWeek;
        public List<String> shift;
    }

    public static List<ClassSchedule> parseClassesOfWeek(String classesOfWeek) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(classesOfWeek, new TypeReference<>() {});
    }

    public static List<StudyClass> scheduleClasses(List<StudyClass> nonRegisteredClasses, List<StudyClass> registeredStudyClasses) throws Exception {
        Map<StudyClass, Integer> colorMap = scheduleClassesGraph(nonRegisteredClasses, registeredStudyClasses);

        Map<Integer, List<StudyClass>> colorGroups = new HashMap<>();
        for (Map.Entry<StudyClass, Integer> entry : colorMap.entrySet()) {
            colorGroups.computeIfAbsent(entry.getValue(), k -> new ArrayList<>()).add(entry.getKey());
        }

        List<StudyClass> maxCreditClasses = new ArrayList<>();
        int maxTotalCredits = 0;
        int maxClassesPerGroup = 10 - registeredStudyClasses.size();

        for (List<StudyClass> group : colorGroups.values()) {
            int groupCredits = group.stream().mapToInt(cls -> cls.getSubject().getCredit()).sum();
            if (groupCredits > maxTotalCredits) {
                maxTotalCredits = groupCredits;
                maxCreditClasses = group.stream().limit(maxClassesPerGroup).toList();
            }
        }

        return maxCreditClasses;
    }

    private static Map<StudyClass, Integer> scheduleClassesGraph(List<StudyClass> nonRegisteredClasses, List<StudyClass> registeredStudyClasses) throws Exception {

    List<ClassSchedule> registeredSchedules = new ArrayList<>();
    for (StudyClass cls : registeredStudyClasses) {
        registeredSchedules.addAll(parseClassesOfWeek(cls.getClassesOfWeek()));
    }

    // Filter out classes that overlap with registered schedules or have the same subject as registered classes
    List<StudyClass> filteredClasses = new ArrayList<>();
    Set<Long> registeredSubjects = new HashSet<>();
    for (StudyClass registeredClass : registeredStudyClasses) {
        registeredSubjects.add(registeredClass.getSubject().getId());
    }

    for (StudyClass cls : nonRegisteredClasses) {
        if (!registeredSubjects.contains(cls.getSubject().getId()) && !isOverlappingWithRegistered(registeredSchedules, cls)) {
            filteredClasses.add(cls);
        }
    }

    // Create a graph
    Map<StudyClass, List<StudyClass>> graph = new HashMap<>();
    Set<Long> addedSubjects = new HashSet<>(registeredStudyClasses.stream().map(cls -> cls.getSubject().getId()).toList());
    for (StudyClass cls : filteredClasses) {
        if (!addedSubjects.contains(cls.getSubject().getId())) {
            graph.put(cls, new ArrayList<>());
        }
    }

    // Add edges to the graph
    List<StudyClass> graphClasses = new ArrayList<>(graph.keySet());
    for (int i = 0; i < graphClasses.size(); i++) {
        for (int j = i + 1; j < graphClasses.size(); j++) {
            if (isOverlapping(parseClassesOfWeek(graphClasses.get(i).getClassesOfWeek()), graphClasses.get(j))) {
                graph.get(graphClasses.get(i)).add(graphClasses.get(j));
                graph.get(graphClasses.get(j)).add(graphClasses.get(i));
            }
        }
    }

    // Apply graph coloring
    Map<StudyClass, Integer> colorMap = new HashMap<>();
    for (StudyClass cls : graphClasses) {
        Set<Integer> usedColors = new HashSet<>();
        for (StudyClass neighbor : graph.get(cls)) {
            if (colorMap.containsKey(neighbor)) {
                usedColors.add(colorMap.get(neighbor));
            }
        }
        int color = 0;
        while (usedColors.contains(color)) {
            color++;
        }
        colorMap.put(cls, color);
    }

    return colorMap;
}

    private static boolean isOverlappingWithRegistered(List<ClassSchedule> registeredSchedules, StudyClass studyClass) throws Exception {
        List<ClassSchedule> schedule = parseClassesOfWeek(studyClass.getClassesOfWeek());

        for (ClassSchedule cs1 : schedule) {
            for (ClassSchedule registered : registeredSchedules) {
                if (cs1.dayOfWeek.equals(registered.dayOfWeek) && !Collections.disjoint(cs1.shift, registered.shift)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isOverlapping(List<ClassSchedule> schedule1, StudyClass class2) throws Exception {
        List<ClassSchedule> schedule2 = parseClassesOfWeek(class2.getClassesOfWeek());

        for (ClassSchedule cs1 : schedule1) {
            for (ClassSchedule cs2 : schedule2) {
                if (cs1.dayOfWeek.equals(cs2.dayOfWeek) && !Collections.disjoint(cs1.shift, cs2.shift)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void checkForOverlappingSchedules(String classesOfWeek, List<StudyClass> studyClassesCrossSemester) throws Exception {
        List<ClassSchedule> newClassSchedule = parseClassesOfWeek(classesOfWeek);
        for (StudyClass existingClass : studyClassesCrossSemester) {
            if (isOverlapping(newClassSchedule, existingClass)) {
                throw new Exception("The study class schedule overlaps with an existing class.");
            }
        }
    }
}