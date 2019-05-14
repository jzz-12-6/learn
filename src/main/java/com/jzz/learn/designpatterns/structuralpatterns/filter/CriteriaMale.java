package com.jzz.learn.designpatterns.structuralpatterns.filter;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author jzz
 * @date 2019-5-13
 */
public class CriteriaMale implements Criteria {
    private static final String MALE = "male";
    @Override
    public List<Person> meetCriteria(List<Person> persons) {
        List<Person> malePersons = new ArrayList<>();
        for (Person person : persons) {
            if(MALE.equalsIgnoreCase(person.getGender())){
                malePersons.add(person);
            }
        }
        return malePersons;
    }
}
