import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, catchError, throwError } from 'rxjs';
import { AccountDetails } from '../../model/model';
import { AccountsService } from '../../services/account.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {
  accountFormGroup!: FormGroup;
  currentPage: number = 0;
  pageSize: number = 5;
  accountObservable!: Observable<AccountDetails>;
  operationFormGroup!: FormGroup;
  errorMessage!: string;

  constructor(
    private fb: FormBuilder,
    private accountService: AccountsService
  ) { }

  ngOnInit(): void {
    this.accountFormGroup = this.fb.group({
      accountId: this.fb.control('')
    });
    this.operationFormGroup = this.fb.group({
      operationType: this.fb.control(null, [Validators.required]),
      amount: this.fb.control(0, [Validators.required, Validators.min(1)]),
      description: this.fb.control(null, [Validators.required]),
      accountDestination: this.fb.control(null)
    });
  }

  handleSearchAccount() {
    let accountId: string = this.accountFormGroup.value.accountId;
    this.accountObservable = this.accountService.getAccount(accountId, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(() => err);
      })
    );
  }

  handleAccountOperation(accountDetails: AccountDetails) {
    let operationType = this.operationFormGroup.value.operationType;
    let amount: number = this.operationFormGroup.value.amount;
    let description: string = this.operationFormGroup.value.description;
    let accountDestination: string = this.operationFormGroup.value.accountDestination;

    if (operationType === 'DEBIT') {
      this.accountService.debit(accountDetails.accountId, amount, description).subscribe({
        next: () => {
          alert("Debit success!");
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        },
        error: err => console.log(err)
      });
    } else if (operationType === 'CREDIT') {
      this.accountService.credit(accountDetails.accountId, amount, description).subscribe({
        next: () => {
          alert("Credit success!");
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        },
        error: err => console.log(err)
      });
    } else if (operationType === 'TRANSFER') {
      this.accountService.transfer(accountDetails.accountId, accountDestination, amount, description).subscribe({
        next: () => {
          alert("Transfer success!");
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        },
        error: err => console.log(err)
      });
    }
  }

  gotoPage(page: number) {
    this.currentPage = page;
    this.handleSearchAccount();
  }
}
