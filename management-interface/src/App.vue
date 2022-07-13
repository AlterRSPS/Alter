<template>
  <div id="app">
    <div class="app_wrapper">
      <div class="sidebar">
        <div class="sidebar__inner">
          <nav>
            <ul>
              <li>
                <a>
                  <h3>Altar Management Interface</h3>
                </a>
              </li>
              <li>
                <router-link to="/">Overview</router-link>
              </li>
              <li>
                <router-link to="">Server management</router-link>
              </li>
              <li>
                <router-link to="">Player management</router-link>
              </li>
              <li>
                <router-link to="">NPC management</router-link>
              </li>
              <li>
                <router-link to="">Event management</router-link>
              </li>
            </ul>
          </nav>
        </div>
      </div>
      <div class="content">
        <div class="content__inner">
          <router-view/>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }

  body {
    font-family: 'Poppins', sans-serif;
    font-weight: 400;
    line-height: 1;
  }

  #app {
    display: block;
    width: 100%;

    .app_wrapper {
      display: flex;
      flex-wrap: wrap;
    }

    .sidebar {
      width: 300px;
      position: fixed;
      top: 0;
      left: 0;
      min-height: 100vh;
      background-color: #475C7A;

      &__inner {
        ul {
          list-style: none;
          padding: 0;
          margin: 0;

          li {
            width: 100%;

            a {
              display: block;
              color: #FFF;
              border-bottom: 1px solid #FFF;
              padding: 20px 10px;
              text-decoration: none;
            }
          }
        }
      }
    }

    .content {
      width: calc(100% - 300px);
      margin-left: 300px;
    }
  }
</style>
