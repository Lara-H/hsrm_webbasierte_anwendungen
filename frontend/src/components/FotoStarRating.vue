<!--
     Star-Rating-Komponente aus der Vorlesung, leicht gekürzt
-->
<template>
  <span class="fotostarrating">
    <a v-for="i in maxsterne" v-bind:key="i" v-on:click="sternGeklickt(i)">
        <i class="fas fa-star" v-bind:class="{ 'checked': i <= sternzahl }"/>
    </a>
    &nbsp;
    <span class="zahlen">{{sternzahl}} / {{maxsterne}}</span>
  </span>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

export default defineComponent({
  name: "FotoStarRating",
  props: {
    maxsterne: { type: Number, required: true, validator: (val: number) => val > 0 },
    sterne:    { type: Number, default: 1 }
  },

  setup(props) {
    // props sind unveränderlich, daher props.sterne nur als Anfangswert
    const sternzahl = ref(props.sterne)

    function sternGeklickt(i: number): void {
        sternzahl.value = i
    }
    return { sternzahl,  sternGeklickt }
  }
});
</script>


<style scoped>
a {
  color: black;
}
.zahlen {
  color: gray;
}
.checked {
  color: orange;
}
</style>

