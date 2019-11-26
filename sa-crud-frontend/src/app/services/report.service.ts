import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Report } from '../model/report';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private baseUrl = 'http://localhost:8081/report';

  constructor(private httpClient: HttpClient) {
  }

  getReport(): Observable<Report> {
    return this.httpClient.get(`${this.baseUrl}`) as Observable<Report>;
  }

}
