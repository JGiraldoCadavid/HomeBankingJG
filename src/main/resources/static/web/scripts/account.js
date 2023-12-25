const { createApp } = Vue

  createApp({
    data() {
      return {
        orderedTransactions:[],
        account:{},
        parameter:null,
        idAccount:0,
        isAccountPage:true,
          from: "",
          until:"",
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
        exportPDF(){
          if(this.from && this.until){
              axios.post(`/api/transactions/exportPdf?id=${this.idAccount}&dateFrom=${this.from}T00:00&dateUntil=${this.until}T23:59`,
                  null,
                  {
                      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                      responseType: 'blob'
                  }
              ).then(response => {
                let fileURL = window.URL.createObjectURL(new Blob([response.data]));
                let fileLink = document.createElement('a');
                fileLink.href = fileURL;
                fileLink.setAttribute('download', `Statement-Account_${this.account.number}_${new Date().toLocaleString()}.pdf`);
                document.body.appendChild(fileLink);
                fileLink.click();
            })
                .catch(error => console.log(error.message))
        } else {
            Swal.fire({
                title: 'Please enter both dates',
                showConfirmButton: false,
                icon: "error",
                showCloseButton: false,
                timer: 2000,
                timerProgressBar: true
            })
        }
          },
    }
  }).mount('#app')