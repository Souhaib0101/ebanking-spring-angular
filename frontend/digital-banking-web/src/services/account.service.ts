import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountDetails } from '../model/model';

@Injectable({
  providedIn: 'root'
})
export class AccountsService {
  private backendHost: string = "http://localhost:8085";

  constructor(private http: HttpClient) { }

  public getAccount(accountId: string, page: number, size: number): Observable<AccountDetails> {
    return this.http.get<AccountDetails>(
      `${this.backendHost}/accounts/${accountId}/pageOperations?page=${page}&size=${size}`
    );
  }

  public debit(accountId: string, amount: number, description: string): Observable<any> {
    let data = { accountId, amount, description };
    return this.http.post(`${this.backendHost}/accounts/debit`, data);
  }

  public credit(accountId: string, amount: number, description: string): Observable<any> {
    let data = { accountId, amount, description };
    return this.http.post(`${this.backendHost}/accounts/credit`, data);
  }

  public transfer(accountSource: string, accountDestination: string, amount: number, description: string): Observable<any> {
    let data = { accountSource, accountDestination, amount, description };
    return this.http.post(`${this.backendHost}/accounts/transfer`, data);
  }
}
