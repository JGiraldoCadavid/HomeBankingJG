const { createApp } = Vue

  createApp({
    data() {
      return {
        typeLoan:null,
        amount:0,
        payments:0,
        destinationAccount:"",
        infoCreate:"",
        loans:[],
        accounts:[],
        clients:[],
        listPayments:[],
        selectedLoan:[],
        maxAmount:0,
        monthlyPayment:0,
        interestRate:0
      }
    },
    created(){  
        this.loadLoans();
        this.loadAccounts();
        this.loadClients();
    },
    methods:{
        loadLoans(){
            axios("/api/loans")
            .then(response=>{
                this.loans=response.data.sort((a, b) => a.name.localeCompare(b.name));
            })
            .catch(error => console.log(error))
        },
        loadAccounts(){
            axios("/api/clients/current/accounts")
            .then(response => {
                this.accounts=response.data.sort((a, b) => a.number.localeCompare(b.number));
            })
            .catch(err => console.log(err))
        },
        loadClients(){
          axios("/api/clients/current")
          .then(response =>{
            console.log(response.data);
              this.clients=response.data;
              console.log(this.clients.clientLoans);
          })
          .catch(err => console.log(err))
        },
        updateListPayments() {

          if (this.typeLoan == null || this.typeLoan == "null") {
            this.amount = 0;
            this.interestRate=0;
            return;
          }
          this.selectedLoan = this.loans.find(loan => loan.id == this.typeLoan);
          if (this.selectedLoan) {
              this.maxAmount = this.selectedLoan.maxAmount;
              this.listPayments = this.selectedLoan.payments;
      
              this.interestRate = this.selectedLoan.interestRate;
              if (this.amount > 0 && this.payments > 0 && this.typeLoan !== null) {
                  this.monthlyPayment = (this.amount * (this.interestRate+1)) / this.payments;
              } else {
                  this.monthlyPayment = 0;
              }
          } else {
              this.maxAmount = 0;
              this.listPayments = [];
              this.monthlyPayment = 0;
          }
        },   
        createLoan() {
          let errorMessage = "";
        
          if (!this.typeLoan) {
            errorMessage = "Please select the loan type.";
          } else if (!this.payments) {
            errorMessage = "Please enter the number of payments.";
          } else if (isNaN(this.payments)) {
            errorMessage = "Please enter a valid numeric value for the number of payments.";
          } else if (this.payments <= 0) {
            errorMessage = "The number of payments must be greater than zero.";
          } else if (!this.amount) {
            errorMessage = "Please enter the loan amount.";
          } else if (isNaN(this.amount)) {
            errorMessage = "Please enter a valid numeric value for the loan amount.";
          } else if (this.amount <= 0) {
            errorMessage = "The loan amount must be greater than zero.";
          } else if (!this.destinationAccount) {
            errorMessage = "Please enter the destination account.";
          }
          
          console.log(this.clients.clientLoans);

          let slcLoan = this.loans.find(loan => loan.id == this.typeLoan);
        
          if (errorMessage) {
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: errorMessage,
            });
            this.typeLoan = null;
                this.amount = 0;
                this.payments = 0;
                this.destinationAccount = '';
          } else {
            Swal.fire({
              title: 'Are you sure to apply?',
              showDenyButton: true,
              confirmButtonText: 'Sure',
              denyButtonText: `I'm not sure`,
            }).then((result) => {
              if (result.isConfirmed) {
                const infoCreate = {
                  id: this.typeLoan,
                  amount: this.amount,
                  payments: this.payments,
                  destinationAccount: this.destinationAccount,
                };
        
                axios.post('/api/loans', infoCreate)
                  .then(async () => {
                    await Swal.fire('Your loan request has been processed successfully. The approved amount is now available in your account.', '', 'success');
                    this.typeLoan = null;
                    this.amount = 0;
                    this.payments = 0;
                    this.destinationAccount = '';
                    location.href = '/web/pages/accounts.html';
                  })
                  .catch((err) => {
                    if(slcLoan.maxAmount<this.amount){
                      Swal.fire({
                          icon: 'error',
                          text: 'The value exceeds the maximum loan amount.'
                      });
                      this.typeLoan = null;
                      this.amount = 0;
                      this.payments = 0;
                      this.destinationAccount = '';
                  }
                  else if (this.clients.clientLoans.some(clientLoan => clientLoan.loanId == this.typeLoan)) {
                    Swal.fire('Error', 'You already have a loan of this type.', 'error');
                    this.typeLoan = null;
                      this.amount = 0;
                      this.payments = 0;
                      this.destinationAccount = '';
                  }                 
                  else {
                    console.log();
                    Swal.fire('Error', 'An error occurred while processing your loan request.', 'error');
                    console.log(err);
                    this.typeLoan = null;
                    this.amount = 0;
                    this.payments = 0;
                    this.destinationAccount = '';
                  }
                });                  
              } else if (result.isDenied) {
                Swal.fire('The loan was not made.', '', 'info');
                this.typeLoan = null;
                this.amount = 0;
                this.payments = 0;
                this.destinationAccount = '';
              }
            });
          }
        },
        returnToAccountsPage(){
            location.href = "/web/pages/accounts.html"
        }
       
    }
  }).mount('#app')