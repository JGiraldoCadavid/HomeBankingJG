const { createApp } = Vue

createApp({
  data() {
    return {
      color: "",
      type: "",
      infoCards: ""
      
    }
  },
  created() {
  },
  methods: {
    createCard() {
      let errorMessage = "";

      if (!this.type && !this.color) {
        errorMessage = "Please select a card type and color.";
      } else if (!this.type) {
        errorMessage = "Please select a card type.";
      } else if (!this.color) {
        errorMessage = "Please select a card color.";
      }

      if (errorMessage) {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: errorMessage,
        });
      } else {
        this.infoCards = `color=${this.color}&type=${this.type}`;

        axios.post("/api/clients/current/cards", this.infoCards)
          .then(async () => {
            console.log("Card created successfully");
            await Swal.fire({
              icon: 'success',
              text: 'Card created successfully.'
            })
            location.href = "/web/pages/cards.html";
          })
          .catch(error => {
            console.error("Error creating card:", error);
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: 'There was an error creating the card. Please select a different card type and try again.',
            });
          });
      }
    },
    returnToCardsPage() {
      location.href = './cards.html';
    },
  }
}).mount('#app')
