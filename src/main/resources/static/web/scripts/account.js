const { createApp } = Vue

  createApp({
    data() {
      return {
        orderedTransactions:[],
        account:{},
        parameter:null,
        idAccount:0,
        isAccountPage:true
      }
    },
    created(){
      this.parameter=location.search;
      let params= new URLSearchParams(this.parameter);
      this.idAccount=params.get("id");
      this.loadClients()
      this.loadData() 
    },
    methods:{
      loadClients(){
        axios("/api/clients/current")
        .then(response =>{
            this.clients=response.data;
        })
        .catch(err => console.log(err))
      },
      loadData(){
        axios('/api/accounts/'+this.idAccount)
        .then(response => {
            this.account=response.data;
            this.orderedTransactions=this.account.transactions.sort((transact1,transact2)=>{
                return transact2.id - transact1.id
            })
        })
        .catch(error => {
          console.error('Error loading data:', error);
          if (error.response && (error.response.status === 403 || error.response.status === 404)) {
            this.logOut();
          }
        });
      },
      formatDate(date) {
        return new Date(date).toLocaleDateString();
      },
      formatTime(date) {
        return new Date(date).toLocaleTimeString();
      },
     logOut(){
      axios.post('/api/logout')
      .then(() => {
        console.log('signed out!!!')
        window.location.href = '/web/index.html';
      })
      .catch(err=>console.log("error: "+err))
     },
     deactivate(){
      if (this.account.balance !== 0) {
        Swal.fire({
          icon: 'error',
          text: 'Cannot delete an account with a non-zero balance.',
        });
        return;
      }
      let infoDeactivate =`id=${this.idAccount}`
      axios.patch('/api/clients/current/accounts/deactivate',infoDeactivate)
      .then( async() => {
        await Swal.fire({
          icon: 'success',
          text: 'Account eliminated successfully.'
        })
        location.href='/web/pages/accounts.html'
      })
      .catch(err => {
        console.log(err)
        Swal.fire({
          icon: 'error',
          text: 'An error occurred while deleting the account. If you only have one account you cannot delete it, so ask the bank to delete your username.'
        })
      })
     },
    }
  }).mount('#app')