import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmployeeList } from '../model/employee-list';
import { Employee } from '../model/employee';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private baseUrl = 'http://localhost:8080/employees';

  constructor(private httpClient: HttpClient) {
  }

  getEmployeesList(pageNumber: number, pageSize: number): Observable<EmployeeList> {
    return this.httpClient.get(`${this.baseUrl}?pageNumber=${pageNumber}&pageSize=${pageSize}`) as Observable<EmployeeList>;
  }

  createEmployee(employee: Employee): Observable<Employee> {
    return this.httpClient.post(`${this.baseUrl}`, employee) as Observable<Employee>;
  }

  updateEmployee(id: number, employee: Employee): Observable<Employee> {
    return this.httpClient.put(`${this.baseUrl}/${id}`, employee) as Observable<Employee>;
  }

  deleteEmployee(id: number) {
    return this.httpClient.delete(`${this.baseUrl}/${id}`);
  }

}
