package dev.chiedo.employee;

import org.mapstruct.Mapper;


@Mapper(componentModel = "cdi")
public interface EmployeeMapper {

    EmployeeEntity toEntity(Employee domain);

    Employee toDomain(EmployeeEntity entity);
}
