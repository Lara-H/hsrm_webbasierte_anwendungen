<template>
  <section id="app">
    <div class="hero is-primary">
      <div class="hero-body">
        <h1 class="title">MI Foto Community</h1>
      </div>
    </div>

    <div class="block">
      <nav class="navbar">
        <div class="navbar-menu is-active">
          <router-link to="/" class="navbar-item">Übersicht</router-link>
          <router-link to="/about" class="navbar-item">About</router-link>
        </div>
        <div class="navbar-end">
          <div class="navbar-item">
            <a href="/logout" class="navbar-item">Logout</a>
          </div>
        </div>
      </nav>
    </div>

    <div class="block">
      <div class="container">
          <router-view />
      </div>
    </div>
  </section>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "App",
  components: {}
});
</script>
