const { createApp } = Vue

  createApp({
    data() {
      return {
        accounts:[],
        selectedOriginAccount: "",
        amount: 0,
        clientLoanId:null,
        loanToPay:[],
        payments:1,
        selectedLoan:"",
        loans:[]
      }
    },
    created(){
        this.loadClients()
        this.loadLoans()
    },
    computed:{
        availableDestinationAccounts() {
            return this.accounts.filter((account) => account.number != this.selectedOriginAccount).sort((a, b) => a.number.localeCompare(b.number));
        }
    },
    methods:{
        loadLoans(){
            axios("/api/loans")
            .then(response=>{
                this.loans=response.data.sort((a, b) => a.name.localeCompare(b.name));
            })
            .catch(error => console.log(error))
        },
    loadClients(){
        axios("/api/clients/current")
        .then(response => {
            this.client=response.data;
            this.loanToPay=this.client.clientLoans;
            console.log(this.loanToPay);
            this.accounts=this.client.accounts;            
        })
        .catch(err => console.log(err))
    },
    returnAccounts(){
        location.href = '/web/pages/accounts.html';
    },
    amountSelectedLoan() {
        console.log(this.clientLoanId);
        
        if (this.clientLoanId === null || this.clientLoanId === "null") {
            this.amount = 0;
            return;
        }
        console.log(this.amount);
        this.selectedLoan = this.loanToPay.find(loan => loan.id == this.clientLoanId);
        console.log(this.selectedLoan);
        if (this.selectedLoan) {
            const unroundedAmount = (this.selectedLoan.amount / this.selectedLoan.payments);
            this.amount = Math.round(unroundedAmount * 100) / 100;
            if(this.selectedLoan.payments -this.selectedLoan.paymentsPaid==1){
                const unroundedAmountFinal =this.selectedLoan.amount-this.selectedLoan.amountPaid
                this.amount = Math.round(unroundedAmountFinal * 100) / 100;
            }
        }
        console.log(this.amount);
    },
    payLoan(){
        if (!this.clientLoanId) {
            Swal.fire({
              icon: 'warning',
              title: 'Oops...',
              text: 'Please select a loan type.',
            });
            return;
          }
          if (!this.selectedOriginAccount) {
            Swal.fire({
              icon: 'warning',
              title: 'Oops...',
              text: 'Please select the source account.',
            });
            return;
        }


        this.amountSelectedLoan();
        console.log(this.amount);
        const accountToUpdate = this.accounts.find(account => account.number == this.selectedOriginAccount);
        console.log(accountToUpdate);
        if (!accountToUpdate) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Selected account not found.',
        });
        return;
        }
        if (accountToUpdate.balance < parseFloat(this.amount)) {
            console.log(this.amount);
        Swal.fire({
            icon: 'error',
            title: 'Insufficient Balance',
            text: 'The selected account does not have sufficient balance for this payment.',
        });
        return;
        }

        if (this.selectedLoan.amount- this.selectedLoan.amountPaid < this.amount * this.payments) {
            console.log(this.amount);
            Swal.fire({
                icon: 'error',
                title: 'Loan Amount Error',
                text: 'The loan amount is less than the total payment amount. Please check the loan details.',
            });
            return;
        }

        let infoPay={
            clientLoanId: this.clientLoanId,
            amountPaid: this.amount,
            payments: this.payments,
            numberAccount: this.selectedOriginAccount
        }

        axios.post('/api/loans/pay', infoPay)
        .then(() => {
            Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: 'Payment successful!',
                showConfirmButton: true,
                confirmButtonText: 'OK'
            })
            accountToUpdate.balance -= parseFloat(this.amount);
            console.log(accountToUpdate);
            setTimeout(function() {
                location.reload();
            }, 3000);
            this.clientLoanId = null;
            this.selectedOriginAccount = "";
            this.amount = 0;
            })
            .catch((err) => {
              console.log(err);
              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Payment failed. Please try again.',
              });
            this.clientLoanId = null;
            this.selectedOriginAccount = "";
            this.amount = 0;
            })
    },
 }
}).mount('#app')