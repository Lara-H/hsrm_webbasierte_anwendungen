<template>
  <div class="container">
    <div v-if="fotostate.errormessage != ''" class="notification is-danger">
      {{ fotostate.errormessage }}
    </div>
    <div class="block">
      <button class="button" v-on:click="fotoGeklickt()">
        <i class="fas fa-camera" />
      </button>
    </div>
    <div class="block">
      <div class="field">
        <label class="label">Suche</label>
        <div class="control has-icons-left">
          <input type="text" class="input" v-model="suchfeld" placeholder="Suche" />
          <span class="icon is-small is-left"><i class="fas fa-search"></i></span>
        </div>
      </div>
    </div>
    <div class="block">
      <div class="columns is-multiline">
        <FotoGalerieBild
          :foto="i"
          v-for="i in listitems"
          v-bind:key="i.id"
          @delete-zeile="deleteFoto($event)"
        />
      </div>
    </div>
    <div class="block">
      <p>Anzahl Bilder: {{fotostate.fotos.length}}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref, Ref, reactive, onMounted } from "vue";
import FotoGalerieBild from "./FotoGalerieBild.vue";
import { Foto } from "@/services/Foto";
import { fotoliste } from "@/services/FotoListe";
import { useFotoStore } from "../services/FotoStore";

export default defineComponent({
  name: "FotoGalerie",
  components: { FotoGalerieBild },
  props: {},
  setup(props) {
    const { fotostate, updateFotos, deleteFotos } = useFotoStore();

    const neuigkeiten = ref("");
    const suchfeld = ref("");

    const listitems = computed(() => {
      const n: number = suchfeld.value.length;
      if (suchfeld.value.length < 3) {
        return fotostate.fotos;
      } else {
        return fotostate.fotos.filter((e) =>
          e.ort.toLowerCase().includes(suchfeld.value.toLowerCase())
        );
      }
    });

    onMounted(async () => {
      await updateFotos();
    });

    function deleteFoto(id: number): void {
      deleteFotos(id);
    }

    /*
    function fotoGeklickt(): void {
      let length = fotos.a.length;
      if(length == fotoliste.length) {
        alert('Keine Fotos mehr');
      } else {
        fotos.a.push(fotoliste[length]);
      }
    }
    */

    return { suchfeld, listitems, neuigkeiten, fotostate, deleteFoto };
  },
});
</script>

<style scoped></style>
