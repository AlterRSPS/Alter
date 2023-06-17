<template>
  <div>
    <p>Player: {{name}}</p>
    <p>Exprate: {{information.xpRate}}</p>
    <p>isOnline: {{information.isOnline}}</p>
    <p>gameMode: {{information.gameMode}}</p>
    <p>Privilege: {{information.privilege}}</p>
    <p>combatLvl: {{information.combatLvl}}</p>

    <br /><br /><br />
    Skills:
    <ul>
      <li v-for="skill in information.skills" v-bind:key="skill.id">
        {{skill.name}} - LVL {{skill.currentLevel}}
      </li>
    </ul>

    <iframe v-if="information != null" width="100%" height="500px" :src="'https://explv.github.io/?centreX='+information.position[0].xPos+'&centreY='+information.position[0].xPos+'&centreZ='+information.position[0].yPos+'&zoom=9'"></iframe>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "PlayerView",
  data() {
    return {
      name: this.$route.params.name,
      information: null
    }
  },
  created() {
    this.fetchPlayer();
  },
  methods: {
    fetchPlayer() {

      axios.get("http://localhost:4567/player/" + this.$route.params.name)
          .then(res => {
            this.information = res.data.player[0]
          })
          .catch(err => {
            console.log(err);
          })
    }
  }
}
</script>

<style scoped>

</style>