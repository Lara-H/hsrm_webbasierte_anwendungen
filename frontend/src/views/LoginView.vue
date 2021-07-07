<template>
  <div class="container">
    <div v-if="loginstate.errormessage != ''" class="notification is-danger">
      {{ loginstate.errormessage }}
    </div>
    <h1 class="title">Login</h1>
    <div>
      <div class="field">
        <label class="label">Nutzername</label>
        <div class="control has-icons-left">
          <input
            class="input"
            type="text"
            v-model="username"
            placeholder="Nutzername"
          />
          <span class="icon is-small is-left">
            <i class="fas fa-user"></i>
          </span>
        </div>
      </div>
      <div class="field">
        <label class="label">Passwort</label>
        <div class="control has-icons-left">
          <input
            class="input"
            type="password"
            v-model="password"
            placeholder="Passwort"
          />
          <span class="icon is-small is-left">
            <i class="fas fa-lock"></i>
          </span>
        </div>
      </div>
      <div class="control">
        <button v-on:click="login()" class="button is-primary">Anmelden</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { useLoginStore } from "@/services/LoginStore";
import { useRouter } from 'vue-router';

export default defineComponent({
  name: "LoginView",
  setup() {
    const username = ref("");
    const password = ref("");
    const { loginstate, doLogout, doLogin } = useLoginStore();
    const router = useRouter();
    doLogout();

    async function login() {
      console.log(JSON.stringify(loginstate))
      if (!loginstate.isLoggedIn) {
        if (await doLogin(username.value, password.value)) {
          router.replace("/");
        }
      } else {
        doLogout();
      }
    }

    return { loginstate, doLogin, username, password, login };
  },
});
</script>
