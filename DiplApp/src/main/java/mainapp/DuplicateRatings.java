package mainapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DuplicateRatings {
    
    private List<StudentDupRating> ddResults;


    public DuplicateRatings() {
        ddResults = new ArrayList<>();
    }

    
    public void setStudentDuplicateRating(String student, String module, double rating) {
        getStudentRating(student).getModuleRating(module).totalModuleDupRating = rating;
    }
    
    public void setStudentPairwiseDuplicateRatings(String student, String module, Map<String, Double> ratings) {
        List<PairwiseDupRating> pairwiseRatings = getStudentRating(student).getModuleRating(module).pairwiseDupRatings;
        for (Map.Entry<String, Double> e : ratings.entrySet()) {
            pairwiseRatings.add(new PairwiseDupRating(e.getKey(), e.getValue()));
        }
    }
    
    
    public List<StudentDupRating> getStudentRatings() {
        return ddResults;
    }
    
    
    private StudentDupRating getStudentRating(String student) {
        if (ddResults == null) return null;
        for (StudentDupRating ss : ddResults) {
            if (ss.studentName.equals(student)) return ss;
        }
        StudentDupRating studentScore = new StudentDupRating(student);
        ddResults.add(studentScore);
        return studentScore;
    }
    
    
    public class StudentDupRating {
        private String studentName;
        private List<ModuleDupRating> moduleDupRatings;
        
        private StudentDupRating(String student) {
            studentName = student;
            moduleDupRatings = new ArrayList<>();
        }
        
        private ModuleDupRating getModuleRating(String moduleName) {
            if (moduleDupRatings == null) return null;
            for (ModuleDupRating ms : moduleDupRatings) {
                if (ms.moduleName.equals(moduleName)) return ms;
            }
            ModuleDupRating moduleScore = new ModuleDupRating(moduleName);
            moduleDupRatings.add(moduleScore);
            return moduleScore;
        }

        public String getStudent() {
            return studentName;
        }

        public List<ModuleDupRating> getModuleRatings() {
            return moduleDupRatings;
        }
    }
    
    public class ModuleDupRating {
        private String moduleName;
        private double totalModuleDupRating;
        private List<PairwiseDupRating> pairwiseDupRatings;
        
        private ModuleDupRating(String module) {
            moduleName = module;
            totalModuleDupRating = Double.NaN;
            pairwiseDupRatings = new ArrayList<>();
        }

        public String getModuleName() {
            return moduleName;
        }

        public double getTotalRating() {
            return totalModuleDupRating;
        }

        public List<PairwiseDupRating> getPairwiseRatings() {
            return pairwiseDupRatings;
        }
    }
    
    public class PairwiseDupRating {
        private String comparisonStudentName;
        private double comparisonRating;
        
        private PairwiseDupRating(String student, double rating) {
            comparisonStudentName = student;
            comparisonRating = rating;
        }

        public String getComparisonStudent() {
            return comparisonStudentName;
        }

        public double getComparisonRating() {
            return comparisonRating;
        }
    }

}
