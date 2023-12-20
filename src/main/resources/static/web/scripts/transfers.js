const { createApp } = Vue

  createApp({
    data() {
      return {
        accounts:[],
        selectedOriginAccount: "",
        selectedDestinationAccount: "",
        destinationAccount: "", 
        destinationType: "",
        description:"",
        amount:0.0,
        infoTransfer:"",
        isAccountPage: false,
        accountId:0,

      }
    },
    created(){
        this.loadAccounts()
    },
    computed:{
        availableDestinationAccounts() {
            return this.accounts.filter((account) => account.number != this.selectedOriginAccount).sort((a, b) => extractNumber(a.number) - extractNumber(b.number));

        }
    },
    methods:{
     loadAccounts(){
        axios('/api/clients/current/accounts')
        .then(response => {
            this.accounts=response.data

            console.log(this.accounts)
        })
        .catch(err => console.log(err))
     },
     extractNumber(accountNumber) {
        const match = accountNumber.match(/(\d+)$/);
        return match ? parseInt(match[1]) : 0;
     },
     transfer(){

        if (this.destinationAccount == "") {
            Swal.fire({
                icon: 'error',
                text: 'Please select the type of account (Own or Others).'
            });
            return;
        }

        if (this.selectedOriginAccount == "") {
            Swal.fire({
                icon: 'error',
                text: 'Please select an origin account.'
            })
            return;
        }
    
        if (this.selectedDestinationAccount == "") {
            Swal.fire({
                icon: 'error',
                text: 'Please select a destination account.'
            })
            return;
        }
    
        if (this.amount == "" || this.amount<=0) {
            Swal.fire({
                icon: 'error',
                text: 'Please enter a valid value (greater than zero).'
            })
            return;
        }
    
        if (this.description == "") {
            Swal.fire({
                icon: 'error',
                text: 'Please enter a description.'
            })
            return;
        }

        if (this.destinationAccount == "others" && this.accounts.some(account => account.number == this.selectedDestinationAccount)) {
            Swal.fire({
                icon: 'error',
                text: 'You cannot transfer to your own account. Select the type of account, own.'
            })
        return;
        }

        let slcOriginAccount = this.accounts.find(account => account.number == this.selectedOriginAccount);

        if (this.amount <= 0 || this.amount > slcOriginAccount.balance) {
            Swal.fire({
                icon: 'error',
                text: 'Invalid transfer amount. Please enter a valid amount.'
            });
            return;
        }

        this.infoTransfer = `amount=${this.amount}&description=${this.description}&sourceAccountNumber=${this.selectedOriginAccount}&destinationAccountNumber=${this.selectedDestinationAccount}`

        axios.post("/api/transactions",this.infoTransfer)
        .then(async (response)=>{
            this.accountId= response.data;
            
            await Swal.fire({
                icon: 'success',
                text: 'Transfer completed successfully.'
            })
            
            location.href = "/web/pages/account.html?id=" + this.accountId;

            this.destinationAccount= "" 
            this.selectedOriginAccount= ""
            this.selectedDestinationAccount= ""
            this.description=""
            this.amount=0.0
              
        })
        .catch(err => {
            Swal.fire({
                icon: 'error',
                text: 'Error transferring. Validate the data and try again.'
            })
            console.log("error: "+err);
        })
     },
     logOut(){
        axios.post('/api/logout')
        .then(() => {
          console.log('signed out!!!')
          window.location.href = '/web/index.html';
        })
        .catch(err=>console.log("error: "+err))
       },
    }
  }).mount('#app')