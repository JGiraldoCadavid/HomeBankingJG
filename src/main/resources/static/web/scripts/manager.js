const { createApp } = Vue

  createApp({
    data() {
      return {
        name:"",
        lastName:"",
        email:"",
        campoVacio:"",
        dataClients: {}
      }
    },

    created(){
        this.loadData();
    },

    methods: {
        loadData(){
            axios('http://localhost:8080/rest/clients')
                .then(data => {
                    this.dataClients=data.data;
                    console.log(this.dataClients);
                })
                .catch(err => console.log(err))
        },

        addClient(){

            const newName=this.name;
            const newLastName=this.lastName;
            const newEmail=this.email;

            if(!newName || !newLastName || !newEmail){
                this.campoVacio="No se puede agregar un cliente, debe llenar todos los campos."

                setTimeout(() => {
                    this.campoVacio = "";
                }, 4000);

                this.name="",
                this.lastName="",
                this.email=""

                return ;
            }

            const newClient={
                name: newName,
                lastName: newLastName,
                email: newEmail
            }
            
            this.postClient(newClient);
        },

        postClient(newClient){

            axios.post('http://localhost:8080/rest/clients', newClient)
            .then(() =>{
                this.dataClients._embedded.clients.push(newClient);
                this.loadData();
            })
            .catch(err => this.campoVacio="Error al guardar el cliente "+err)
            .finally(() =>{
                this.name="",
                this.lastName="",
                this.email=""
            })
        }
    }
  }).mount('#app')