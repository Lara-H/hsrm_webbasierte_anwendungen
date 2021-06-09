<template>
  <div class="home">
    <h1 class="title">Unsere Foto-Galerie</h1>
    <FotoGalerie />
  </div>
</template>

<script lang="ts">
import FotoGalerie from "@/components/FotoGalerie.vue";
import { defineComponent } from "vue";

export default defineComponent({
  name: "FotoGalerieView",
  components: {
    FotoGalerie
  }
});
</script>
