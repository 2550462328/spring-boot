package cn.ictt.zhanghui.springboot_test.repository;

import cn.ictt.zhanghui.springboot_test.SpringbootTestApplicationTests;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.Employee;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.EmployeeRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/9/22 14:52
 **/
public class EmployeeRepositoryTest extends SpringbootTestApplicationTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void testSelectTeammatesI(){
        employeeRepository.deleteAll();

        Employee greg = new Employee("Greg");
        Employee roy = new Employee("Roy");
        Employee craig = new Employee("Craig");

        List<Employee> team = Arrays.asList(greg, roy, craig);

        System.out.println("Before linking up with Neo4j...");

        team.stream().forEach(person -> System.out.println("\t" + person.toString()));

        employeeRepository.save(greg);
        employeeRepository.save(roy);
        employeeRepository.save(craig);

        greg = employeeRepository.findByName(greg.getName());
        greg.worksWith(roy);
        greg.worksWith(craig);
        employeeRepository.save(greg);

        roy = employeeRepository.findByName(roy.getName());
        roy.worksWith(craig);
        // We already know that roy works with greg
        employeeRepository.save(roy);

        // We already know craig works with roy and greg
        System.out.println("Lookup each person by name...");
        team.stream().forEach(person -> System.out.println(
                "\t" + employeeRepository.findByName(person.getName()).toString()));

        List<Employee> teammates = employeeRepository.findByTeammatesName(greg.getName());
        System.out.println("The following have Greg as a teammate...");
        teammates.stream().forEach(person -> System.out.println("\t" + person.getName()));
    }
}
