package dev.chiedo.employee;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@ApplicationScoped
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    // constructor injection
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Employee> findById(long employeeId) {
        return employeeRepository.findByIdOptional(employeeId).map(employeeMapper::toDomain);
    }

    @Transactional
    public void save(Employee employee) {
        EmployeeEntity employeeEntity = employeeMapper.toEntity(employee);
        employeeRepository.persist(employeeEntity);
    }

    @Transactional
    public void update(long employeeId, Employee employee) {
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByIdOptional(employeeId);

        if (optionalEmployeeEntity.isEmpty()) {
            throw new NotFoundException(String.format("No Employee found with employeeId[%s]", employee.getEmployeeId()));
        }

        EmployeeEntity employeeEntity = optionalEmployeeEntity.get();

        employeeEntity.setEmployeeId(employeeId);
        employeeEntity.setFirstName(employee.getFirstName());
        employeeEntity.setMiddleName(employee.getMiddleName());
        employeeEntity.setLastName(employee.getLastName());
        employeeEntity.setDepartment(employee.getDepartment());
        employeeEntity.setEmailAddress(employee.getEmailAddress());
        employeeEntity.setPhoneNumber(employee.getPhoneNumber());

        employeeRepository.persist(employeeEntity);
    }

    @Transactional
    public void delete(Employee employee) {
        EmployeeEntity employeeEntity = employeeMapper.toEntity(employee);

        employeeRepository.delete(employeeEntity);
    }
}
