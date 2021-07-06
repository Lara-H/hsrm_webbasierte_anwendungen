<template>
  <div class="container">
    <div v-if="loginstate.errormessage != ''" class="notification is-danger">
      {{ losinstate.errormessage }}
    </div>
    <h1 class="title">Login</h1>
    <form>
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
        <button class="button is-primary" type="submit">Anmelden</button>
      </div>
    </form>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { useLoginStore } from "@/services/LoginStore";

export default defineComponent({
  name: "LoginView",
  setup() {
    const username = ref("");
    const password = ref("");
    const { loginstate, doLogout, doLogin } = useLoginStore();
    doLogout();

    return { loginstate, doLogin, username, password };
  },
});
</script>
