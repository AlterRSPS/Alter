<template>
  <div class="home">
    <div class="block">
      <div class="block__inner">
        {{playerCount}} players online
      </div>
    </div>
    <div class="block">
      <div class="block__inner">
        {{adminCount}} admins online
      </div>
    </div>
    <div class="block list">
      <div class="block__inner">
        <ul>
          <li v-for="player in players" v-bind:key="player.username">
            <router-link :to="'/player/'+player.username">{{player.username}}</router-link>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import axios from 'axios';

export default {
  name: 'HomeView',
  data() {
    return {
      playerCount: 0,
      adminCount: 0,
      players: []
    }
  },
  created() {
    this.fetchOnlinePlayers();

    window.setInterval(() => {
      this.fetchOnlinePlayers();
    }, 5000);
  },
  methods: {
    fetchOnlinePlayers() {
      axios.get("http://localhost:4567/players")
        .then(res => {
          this.playerCount = res.data.count;
          this.adminCount = res.data.players.filter(p => p.privilege > 0).length
          this.players = res.data.players;
        })
        .catch(err => {
          console.log(err);
        })
    }
  }
}
</script>

<style lang="scss">
  .home {
    display: flex;
    flex-wrap: wrap;

    .block {
      width: 33.3333%;
      border: 1px solid #000;
      border-left: none;
      border-top: none;
      height: 150px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 30px;
      padding: 25px;

      &__inner {
        width: 100%;
        text-align: center;
      }

      &.list {
        overflow-y: scroll;
        font-size: 18px;
        justify-content: flex-start;
        align-items: flex-start;

        ul {
          list-style: none;
          width: 100%;

          li {
            width: 100%;

            a {
              display: block;
              padding: 5px 0;
              width: 100%;
              text-align: left;
              border-bottom: 1px solid #000;
              color: #D8737F;
              text-decoration: none;
            }
          }
        }
      }
    }
  }
</style>