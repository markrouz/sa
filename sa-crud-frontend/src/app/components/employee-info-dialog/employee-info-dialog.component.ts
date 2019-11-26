import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Employee } from '../../model/employee';

@Component({
  selector: 'app-employee-info-dialog',
  templateUrl: './employee-info-dialog.component.html',
  styleUrls: ['./employee-info-dialog.component.scss']
})
export class EmployeeInfoDialogComponent implements OnInit {

  departments = ['IT', 'SALES', 'FINANCE'];
  positions = ['MANAGER', 'DEVELOPER', 'ANALYST'];

  form: FormGroup;
  title: string;

  constructor(private formBuilder: FormBuilder,
              private dialogRef: MatDialogRef<EmployeeInfoDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public employee: Employee) {
    this.form = formBuilder.group({
      nameControl: ['', Validators.required],
      surnameControl: ['', Validators.required],
      patronymicControl: [''],
      departmentControl: [''],
      positionControl: [''],
      employmentDateControl: [''],
      firedDateControl: ['']
    });

  }

  ngOnInit() {
    if (this.employee) {
      this.title = 'Edit employee';
      this.buildFormFromEmployee(this.employee);
    } else {
      this.title = 'Add new Employee';
    }
  }

  cancel() {
    this.dialogRef.close();
  }

  save() {
    this.dialogRef.close(this.buildEmployeeFromForm());
  }

  private buildEmployeeFromForm(): Employee {
    const employmentDateControlValue = this.form.get('employmentDateControl').value;
    const firedDateControlValue = this.form.get('firedDateControl').value;
    return {
      id: null,
      name: this.form.get('nameControl').value,
      surname: this.form.get('surnameControl').value,
      patronymic: this.form.get('patronymicControl').value,
      department: this.form.get('departmentControl').value,
      position: this.form.get('positionControl').value,
      employmentDate: employmentDateControlValue === '' || employmentDateControlValue == null ? ''
        : employmentDateControlValue.toISOString().slice(0, 10),
      firedDate: firedDateControlValue === '' || employmentDateControlValue == null ? ''
        : firedDateControlValue.toISOString().slice(0, 10),
    };
  }

  private buildFormFromEmployee(employee: Employee) {
    this.form.get('nameControl').patchValue(employee.name);
    this.form.get('surnameControl').patchValue(employee.surname);
    this.form.get('patronymicControl').patchValue(employee.patronymic);
    this.form.get('departmentControl').patchValue(employee.department);
    this.form.get('positionControl').patchValue(employee.position);
    this.form.get('employmentDateControl').patchValue(this.employee.employmentDate === null ? '' : new Date(this.employee.employmentDate));
    this.form.get('firedDateControl').patchValue(this.employee.firedDate === null ? '' : new Date(this.employee.firedDate));
  }
}
