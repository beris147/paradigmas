<template>
  <div id="menu" class="stickyMain">
    <b-navbar class="col-xl-4 col-lg-5 col-md-6 col-sm-7 col-11 mx-auto p-0" id="menuBar">
      <b-navbar-brand href="https://www.facebook.com/chismenolikeoficial/?__tn__=kC-R&eid=ARAVcBRwbO_fvhuyeCy6ouDriLxWDYHv3m3V92HqmeOA3S5scaay0hhnEOcefSFMJoTS3HO850UDOISu&hc_ref=ARRFn0goJ3w9Qh4mVQj3ZlKn0Amt1fhP35vvQrx1TiNlAHFLy3wTRpu_shVqpKBkJ3M&fref=nf&__xts__[0]=68.ARABiDdjRoE6nIWrVd4QSbEkQNeuQUNL5wSuQ63JxQD2bUHqBn0dbuJaoMtACn49C1cP57zsU3XFM3JCE2Hs2Q-KZctnDwpsJQWtFieZPNTCpnhytzmhuwGTRfJxypCLGlLNoZkUibCNPHZoq7sW-K0n_UR5zdfCT7HpwR0xvjeoFbiVVY2i5wSXntR_Z4qkssdblnXIYIP-oh9TGXOgScmkAk2-7HG4yNDilzooPdSDdVmyYJlsZHSgmx2IVAkDRI2pVK8_kuHpoHYiwXLEExK4jTk7jDG3TnS1--A8xHYHfSBSEXxR63q-KMlvrQk-ZYnBwyDGROb-0L4" target="_blank">
        <font-awesome-icon class="menuElement mx-xl-4 mx-md-3 mx-sm-2 mx-1" :icon="{ prefix: 'fab', iconName: 'facebook-square' }"/>
      </b-navbar-brand>
      <b-navbar-brand href="https://twitter.com/ChismeNo" target="_blank">
        <font-awesome-icon class="menuElement mx-xl-4 mx-md-3 mx-sm-2 mx-1" :icon="{ prefix: 'fab', iconName: 'twitter-square' }"/>
      </b-navbar-brand>
      <b-navbar-brand href="#">
        <img v-bind:src="logoMenu" alt="Chisme No Like" id="logoMenu" class="mx-auto">
      </b-navbar-brand>
      <b-navbar-brand href="https://www.instagram.com/chismenolikeoficial/" target="_blank">
        <font-awesome-icon class="menuElement mx-xl-4 mx-md-3 mx-sm-2 mx-1" :icon="{ prefix: 'fab', iconName: 'instagram-square' }"/>
      </b-navbar-brand>
      <b-navbar-brand href="https://www.youtube.com/channel/UC5qgk9xFZhXjzvCRcZn8KqQ" target="_blank">
        <font-awesome-icon class="menuElement mx-xl-4 mx-md-3 mx-sm-2 mx-1" :icon="{ prefix: 'fab', iconName: 'youtube-square' }"/>
      </b-navbar-brand>
    </b-navbar>
  </div>
</template>

<script>
    export default {
        name: 'MenuBar',
        data () {
            return {
                logoMenu: '/static/logoMenu.png'
            }
        }
    }
</script>

<style scoped>
    #menu {
        margin: 0;
        padding: 0;
        background-color: transparent;
        top: 1.5rem;
    }

    #menuBar {
        justify-content: center;
        border-radius: 25px;
        border: solid 5px #ec268f;
        height: 50px;
        background-color: white;
    }

    #menuBar, .menuElement:hover {
        box-shadow: -2px 2px 10px -4px black;
    }

    .menuElement:hover {
        color: #ec268f;
    }

    @media only screen and (max-width: 576px) {
        #menuBar {
            height: 35px;
        }

        #logoMenu {
            width: 80%;
            margin-left: 10% !important;
        }
    }
</style>