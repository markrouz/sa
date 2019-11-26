import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  OnChanges, SimpleChanges,
  ViewChild
} from '@angular/core';
import { EmployeeService } from '../../services/employee.service';
import { MatPaginator } from '@angular/material/paginator';
import { Employee } from '../../model/employee';
import { catchError, map, startWith, switchMap, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { EmployeeInfoDialogComponent } from '../employee-info-dialog/employee-info-dialog.component';
import { ConfirmActionDialogComponent } from '../confirm-action-dialog/confirm-action-dialog.component';
import { Report } from '../../model/report';
import { ReportService } from '../../services/report.service';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss'],
})
export class EmployeeListComponent implements AfterViewInit {

  displayedColumns: string[] = ['name', 'surname', 'patronymic', 'department', 'position', 'employmentDate', 'firedDate', 'actions'];
  pageSize = 5;
  employees: Employee[] = [];

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  avgWorkPeriod: string;
  mostPopularDepartments: string;

  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;

  constructor(private employeeService: EmployeeService,
              private reportService: ReportService,
              private employeeInfoDialog: MatDialog,
              private deleteEmployeeDialog: MatDialog) {
  }

  ngAfterViewInit() {
    this.paginator.page
    .pipe(
      startWith({}),
      tap(() => this.employees = []),
      switchMap(() => {
        this.isLoadingResults = true;
        return this.employeeService.getEmployeesList(
          this.paginator.pageIndex, this.pageSize);
      }),
      map(data => {
        this.isLoadingResults = false;
        this.isRateLimitReached = false;
        this.resultsLength = data.totalElements;

        return data.content;
      }),
      catchError(() => {
        this.isLoadingResults = false;
        this.isRateLimitReached = true;
        return of([]);
      })
    ).subscribe(data => this.employees = data);
    this.buildReport();
  }

  openAddEmployeeDialog(): void {
    const dialogRef = this.employeeInfoDialog.open(EmployeeInfoDialogComponent, {
      minWidth: '350px',
      disableClose: true,
      autoFocus: true,
    });
    dialogRef.afterClosed().subscribe(data => {
      if (data) {
        this.employeeService.createEmployee(data).subscribe(employee => {
          this.employees.push(employee);
          this.resultsLength += 1;
          this.buildReport();
        });
      }
    });
  }

  deleteEmployee(employee: Employee) {
    const dialogRef = this.deleteEmployeeDialog.open(ConfirmActionDialogComponent, {
      disableClose: true,
      autoFocus: true,
      data: {
        title: 'Confirm action',
        message: 'Delete employee?',
      },
    });

    dialogRef.afterClosed().subscribe(dialogResult => {
      if (dialogResult) {
        this.employeeService.deleteEmployee(employee.id).subscribe(() => this.buildReport());
        this.employees = this.employees.filter(val => val.id !== employee.id);
        this.resultsLength -= 1;
      }
    });
  }

  editEmployee(employee: Employee) {
    const dialogRef = this.employeeInfoDialog.open(EmployeeInfoDialogComponent, {
      minWidth: '350px',
      disableClose: true,
      autoFocus: true,
      data: employee,
    });
    dialogRef.afterClosed().subscribe(data => {
      if (data) {
        this.employeeService.updateEmployee(employee.id, data).subscribe(emp => {
          const index = this.employees.indexOf(employee);
          this.employees[index] = emp;
          this.employees = [].concat(this.employees);
          this.buildReport();
        });
      }
    });
  }

  private buildReport() {
    this.reportService.getReport().subscribe(report => {
      this.avgWorkPeriod = report.avgWorkPeriod.toFixed(0);
      this.mostPopularDepartments = report.mostPopularDepartments.join(', ');
    });
  }
}
