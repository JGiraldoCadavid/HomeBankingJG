const { createApp } = Vue

  createApp({
    data() {
      return {
        client:{},
        debitCards:[],
        creditCards:[],
        isAccountPage: false,
        infoDeactivate: ""
      }
    },
    created(){
        this.loadData()   
    },
    methods:{
        loadData(){
            axios("/api/clients/current/cards")
            .then(response => {
                this.client=response.data;
                this.debitCards=this.client.filter(card => card.type == "DEBIT").sort((a, b) => a.number.localeCompare(b.number));
                ;
                this.creditCards=this.client.filter(card => card.type == "CREDIT").sort((a, b) => a.number.localeCompare(b.number));
                ;               
            })
            .catch(err => console.log(err))
        },
        formatDate(date) {
            return new Date(date).toLocaleString('en-US', { month: '2-digit', year: '2-digit' });
        },
        logOut(){
          axios.post('/api/logout')
          .then(() => {
            console.log('signed out!!!')
            window.location.href = '/web/index.html';
          })
          .catch(err=>console.log("error: "+err))
         },
         createCard(){
          window.location.href = '/web/pages/create-cards.html';
         },
         deactivate(id){
          infoDeactivate =`id=${id}`
          axios.patch('/api/clients/current/cards/deactivate',infoDeactivate)
          .then( async() => {
            await Swal.fire({
              icon: 'success',
              text: 'Card eliminated successfully.'
            })
            location.reload()
          })
          .catch(err => {
            console.log(err)
            Swal.fire({
              icon: 'error',
              text: 'An error occurred while deleting the card. Please try again.'
            })
          })
         },
         isCardExpired(expirationDate) {
          const today = new Date();
          const expiration = new Date(expirationDate);
          return expiration.setHours(0, 0, 0, 0) < today.setHours(0, 0, 0, 0);
        },
    }
  }).mount('#app')