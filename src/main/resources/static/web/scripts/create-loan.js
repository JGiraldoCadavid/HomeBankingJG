const { createApp } = Vue

  createApp({
    data() {
      return {
        name: "",
        maxAmount:0.00,
        payments: "",
        interestRate: 1
      }
    },
    created(){
    },
    methods:{
      createLoan(){

        if (!this.validateName()) {
            Swal.fire('The loan name cannot be empty.', '', 'error');
            return;
          }
      
          if (!this.validateMaxAmount()) {
            Swal.fire('Maximum loan amount must be greater than 50000.', '', 'error');
            return;
          }
      
          if (!this.validatePayments()) {
            Swal.fire('At least 3 valid payments are required.', '', 'error');
            return;
          }
      
          if (!this.validateInterestRate()) {
            Swal.fire('Interest rate must be greater than 0.', '', 'error');
            return;
          }

        const arrayPayments= this.payments.split(',').map(payment => parseInt(payment, 10)).sort((payment1, payment2) => payment1 - payment2);
        this.interestRate/=100
        const infoCreate={
            name: this.name,
            maxAmount: this.maxAmount,
            payments: arrayPayments,
            interestRate: this.interestRate
        }
        console.log(infoCreate);
        axios.post('/api/loans/create', infoCreate)
        .then(() => {
            Swal.fire('Loan created successfully', '', 'success');
            this.name = "";
            this.maxAmount = 0.00;
            this.payments = "";
            this.interestRate=0.00;
        })
        .catch(err => console.log(err))
      },
      logOut(){
        axios.post('/api/logout')
        .then(() => {
          console.log('signed out!!!')
          window.location.href = '/web/index.html';
        })
        .catch(err=>console.log("error: "+err))
       },
       validateName() {
        return this.name.trim() !== "";
      },
      validateMaxAmount() {
        return this.maxAmount > 50000;
      },
      validatePayments() {
        const arrayPayments = this.payments.split(',').map(payment => parseInt(payment, 10));
        return arrayPayments.length > 2 && arrayPayments.every(payment => payment > 0);
      },
      validateInterestRate() {
        return this.interestRate > 0;
      },
    }
  }).mount('#app')