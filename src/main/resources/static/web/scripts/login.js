const { createApp } = Vue

  createApp({
    data() {
      return {
        email:"",
        password:"",
        emailError: false,
        passwordError: false,
        nameError:false,
        lastNameError:false,
        infoLogin:"",
        showLogin: true,
        name: "",
        lastName:"",
        infoRegister:"",
      }
    },
    created(){
    },
    methods:{
        signIn() {
            this.infoLogin = `email=${this.email}&password=${this.password}`;

            this.emailError = !this.isValidEmail(this.email);
            this.passwordError = !this.isValidPassword(this.password);
                
            axios.post('/api/login', this.infoLogin)
              .then(response => {
                console.log("Successful request", response.data);
                this.redirect()
                Swal.fire({
                  position: 'center',
                  icon: 'success',
                  title: 'Welcome',
                  showConfirmButton: false,
                  timer: 1500
              });
          })
              .catch(err => {
                console.log(err);
                Swal.fire({
                  icon: 'error',
                  title: 'Oops...',
                  text: 'Incorrect username and/or password!'
                })
                setTimeout(() => {
                  this.emailError = false;
                  this.passwordError = false;
                }, 5000);
            })
            .finally(() => {
                this.email = "";
                this.password = "";
            });
        },
        toggleView() {
          this.email = "";
          this.password = "";
          this.name = "";
          this.lastName = "";
          this.emailError = false;
          this.passwordError = false;
          this.nameError = false;
          this.lastNameError = false;
          this.showLogin = !this.showLogin;
        },
        signUp(){

          this.infoRegister = `name=${this.name}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`;

          this.emailError = !this.isValidEmail(this.email);
          this.passwordError = !this.isValidPassword(this.password);
          this.nameError = !this.isValidName(this.name);
          this.lastNameError = !this.isValidLastName(this.lastName);

          axios.post('/api/clients',this.infoRegister)
          .then(() => {
            this.signIn()
            console.log('registered')            
            this.redirect()
          })
          .catch(err => {
            console.log(err)
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: 'Registration failed. Please check your information and try again.'
            })
            setTimeout(() => {
              this.emailError = false;
              this.passwordError = false;
              this.nameError = false;
              this.lastNameError = false;
            }, 5000);
          })
          .finally(() => {
            this.email = "";
            this.password = "";
            this.name = "";
            this.lastName = "";
          });
        },
        isValidEmail(email) {
          const emailPattern = /\S+@\S+\.\S+/;
          return emailPattern.test(email);
        },
        isValidPassword(password) {
        return password.length >= 5 && password.trim() != "";
        },
        isValidName(name) {
          return name.length >= 2;
        },
        isValidLastName(lastName) {
          return lastName.length >= 2;
        },
        returnToHomePage() {
          location.href = '../index.html';
        },
        redirect() {
          if (this.email.includes("@admin")) {
              this.redirectAdmin();
          } else {
              this.redirectClient();
          }
      },
      redirectAdmin() {
          setTimeout(() => {
              window.location.href = '/web/pages/create-loan.html';
          }, 1000);
      },
      redirectClient() {
          setTimeout(() => {
              window.location.href = '/web/pages/accounts.html';
          }, 1000);
      },
    }
  }).mount('#app')