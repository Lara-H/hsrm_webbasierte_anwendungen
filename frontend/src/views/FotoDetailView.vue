<template>
  <div class="container">
    <h1 class="title">{{ foto.dateiname }}</h1>
    <section class="section">
      <p>{{ foto.zeitstempel }}</p>
      <img :src="url" /><br />
      <p>{{ foto.ort }}</p>
      <a href="/" class="button is-primary">Zurück</a>
    </section>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useFotoStore } from "../services/FotoStore";

export default defineComponent({
  name: "FotoDetailView",
  props: {
    fotoid: { type: String, required: true },
  },

  setup(props) {
    const { fotostate, updateFotos, deleteFotos } = useFotoStore();

    return {
      foto: fotostate.fotos.find((e) => e.id == parseInt(props.fotoid)),
      url: "/foto/" + parseInt(props.fotoid),
    };
  },
});
</script>
