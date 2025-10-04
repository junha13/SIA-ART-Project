import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import './style.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { quillEditor } from 'vue3-quill'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
const app = createApp(App)

app.use(createPinia())
app.use(router)
app.component('quill-editor', quillEditor)

app.mount('#app')
