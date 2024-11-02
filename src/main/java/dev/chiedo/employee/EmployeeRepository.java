package dev.chiedo.employee;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class EmployeeRepository implements PanacheRepositoryBase<EmployeeEntity, Long> {
}
