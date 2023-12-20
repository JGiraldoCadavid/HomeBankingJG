const { createApp } = Vue;

createApp({
  data() {
    return {};
  },
  created(){  
  },
  methods: {
    signIn() {
      window.location.href = '/web/pages/login.html';
    },
  },
}).mount('#app');