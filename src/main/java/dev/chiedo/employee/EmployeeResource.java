package dev.chiedo.employee;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.net.URI;


@Path("/api/v1/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeResource.class);

    private final EmployeeService employeeService;

    // constructor injection
    public EmployeeResource(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GET
    @Operation(summary = "Returns all existing employees")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Get All Employees",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.ARRAY, implementation = Employee.class))),
                    @APIResponse(
                            responseCode = "404",
                            description = "No employees found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public Response getAllEmployees() {
        return Response.ok(employeeService.findAll()).build();
    }

    @GET
    @Path("/{employeeId}")
    @Operation(summary = "Returns an employee given the employee Id")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Get Employee by employeeId",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.OBJECT, implementation = Employee.class))),
                    @APIResponse(
                            responseCode = "404",
                            description = "No Employee found for the employeeId provided",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public Response getEmployeeById(@PathParam("employeeId") Long employeeId) {
        Optional<Employee> optionalEmployee = employeeService.findById(employeeId);

        if (optionalEmployee.isPresent()) {
            LOGGER.info("Found employee {}", optionalEmployee);
            return Response.ok(optionalEmployee.get()).build();
        } else {
            LOGGER.debug("No employee found with id {}", employeeId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Operation(summary = "Adds a new employee")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "201",
                            description = "Employee created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.OBJECT, implementation = Employee.class))),
                    @APIResponse(
                            responseCode = "400",
                            description = "Employee already exists with employeeId",
                            content = @Content(mediaType = "application/json")),
            }
    )
    public Response createEmployee(@RequestBody(required = true) @Valid Employee employee) {
        employeeService.save(employee);
        URI employeeUrl = URI.create("/api/v1/employees/" + employee.getEmployeeId());
        LOGGER.info("New employee added at URL {}", employeeUrl);
        return Response.created(employeeUrl).build();
    }

    @PUT
    @Path("/{employeeId}")
    @Operation(summary = "Updates an existing employee")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "204",
                            description = "Employee successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.OBJECT, implementation = Employee.class))),
                    @APIResponse(
                            responseCode = "404",
                            description = "No Employee found for employeeId provided",
                            content = @Content(mediaType = "application/json")),
            }
    )
    public Response updateEmployee(@PathParam("employeeId") Long employeeId, @RequestBody @Valid Employee employee) {
        Optional<Employee> optionalEmployee = employeeService.findById(employeeId);

        if (optionalEmployee.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        employeeService.update(employeeId, employee);

        LOGGER.info("Employee with id {} updated successfully", employeeId);

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{employeeId}")
    @Operation(summary = "Deletes an existing employee")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "204",
                            description = "Employee successfully deleted",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.OBJECT, implementation = Employee.class))),
                    @APIResponse(
                            responseCode = "404",
                            description = "No Employee found for employeeId provided",
                            content = @Content(mediaType = "application/json")),
            }
    )
    public Response deleteEmployee(@PathParam("employeeId") Long employeeId) {
        Optional<Employee> optionalEmployee = employeeService.findById(employeeId);

        if (optionalEmployee.isEmpty()) {
            LOGGER.debug("Employee with id {} does not exist", employeeId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        employeeService.delete(optionalEmployee.get());

        LOGGER.info("Employee deleted with id {}", employeeId);

        return Response.noContent().build();
    }
}
