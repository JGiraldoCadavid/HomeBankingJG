const { createApp } = Vue

  createApp({
    data() {
      return {
        client:{},
        accounts:[],
        loans:[],
        isAccountPage: false,
      }
    },
    created(){
        this.loadData()    
    },
    methods:{
        loadData(){
            axios("/api/clients/current")
            .then(response => {
                this.client=response.data;
                console.log(this.client);
                this.loans=this.client.clientLoans.sort((a, b) => a.name.localeCompare(b.name));
                console.log(this.loans);
                this.accounts=this.client.accounts.sort((a, b) => a.number.localeCompare(b.number));
                console.log(this.accounts);
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
        createAccount(){
          Swal.fire({
            title: 'Select Account Type',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'SAVINGS',
            cancelButtonText: 'CHECKING',
            reverseButtons: true
          }).then((result) => {
            if (result.isConfirmed) {
              this.createAccountType("SAVINGS");
            } else {
              this.createAccountType("CHECKING");
            }
          });
        },
        createAccountType(accountType) {
          let infoCreate = `type=${accountType}`
          axios.post('/api/clients/current/accounts', infoCreate)
            .then(() => {
              Swal.fire({
                icon: 'success',
                text: 'Account created successfully.'
              });
              setTimeout(() => {
                location.reload();
              }, 2000);
            })
            .catch(err => {
              Swal.fire({
                icon: 'error',
                text: 'An error occurred while creating the account.'
              });
              console.log("error: " + err);
            });
        },
        applyLoan(){
          location.href = '/web/pages/loan-application.html';
        },
        payLoan(){
          location.href = '/web/pages/pay-loan.html';
        }
    }
  }).mount('#app')