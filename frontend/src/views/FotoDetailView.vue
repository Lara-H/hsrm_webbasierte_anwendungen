<template>
<div class="content">
  <h1>{{foto.dateiname}}</h1>
  <p>{{foto.zeitstempel}}</p>
  <img :src="url"/>
</div>
</template>


<script lang="ts">

import {defineComponent} from "vue";
import { useFotoStore } from "../services/FotoStore";

export default defineComponent({
  name: "FotoDetailView",
  props : {
    fotoid : {type: String, required: true},
  },

  setup(props) {
      const {fotostate, updateFotos, deleteFotos} = useFotoStore()


    return {
      foto : fotostate.fotos.find(e => e.id == parseInt(props.fotoid)),
      url: '/foto/' + parseInt(props.fotoid)
    }
  }
});
</script>
