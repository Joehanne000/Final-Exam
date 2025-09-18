package com.jowhan.employee.Repository;

import com.jowhan.employee.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EmployeeRepository {

    private final Map<Long, Employee> employees = new HashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(employees.get(id));
    }

    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(counter.getAndIncrement());
        }
        employees.put(employee.getId(), employee);
        return employee;
    }

    public boolean existsByEmail(String email) {
        return employees.values().stream()
                .anyMatch(emp -> emp.getEmail().equalsIgnoreCase(email));
    }

    public void deleteById(Long id) {
        employees.remove(id);
    }
}
