import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// FontAwesome-CSS laden
import '@fortawesome/fontawesome-free/css/all.css'

// Bulma-Anpassungen über main.scss
import '@/assets/main.scss'

createApp(App).use(router).mount('#app')
