package com.jzz.learn.designpatterns.structural.filter;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author jzz
 * @date 2019-5-13
 */
public class CriteriaFemale implements Criteria {
    private static final String FEMALE = "FEMALE";
    @Override
    public List<Person> meetCriteria(List<Person> persons) {
        List<Person> femalePersons = new ArrayList<>();
        for (Person person : persons) {
            if(FEMALE.equalsIgnoreCase(person.getGender())){
                femalePersons.add(person);
            }
        }
        return femalePersons;
    }
}
