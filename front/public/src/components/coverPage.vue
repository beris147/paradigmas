<template>
    <div id="coverContainer" class="text-center">
      <b-img v-bind:src="cover" alt="Conductores" class="col-xl-4 col-6" id="coverImg"></b-img>
      <b-img v-bind:src="logo" alt="Chisme No Like" class="col-4" id="coverImgLogo"></b-img>
    </div>
</template>

<script>
  export default {
    name: 'CoverPage',
    data () {
      return {
        cover: '/static/portada.png',
        logo: '/static/logo.png'
      }
    }
  }
</script>

<style scoped>
    #coverContainer {
        padding: 0;
        margin: 0;
        height: auto;
    }

    #coverImg, #coverImgLogo {
      position: relative !important;
      top: 0 !important;
    }

    #coverContainer {
      background-color: #ec268f;
    }

    @media only screen and (max-width: 425px) {
        #coverContainer {
            height: 35vh;
        }
    }

    @media only screen and (max-width: 375px) {
        #coverContainer {
            height: 26vh;
        }
    }
</style>