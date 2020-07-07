<template>
  <div>
    <div id="principal" class="p-md-5 p-3 col-12 m-0">
      <router-link to="/" id="botonRegresar" class="p-0 sticky">
        <img v-bind:src="logo" alt="Chisme No Like" id="logoRegresar">
      </router-link>
      <div id="nota" class="contenido col-lg-8 col-md-10 col-11 mx-auto p-lg-5 p-md-3 pt-5 m-0">
        <p class="col-11 mx-auto mb-5 pTitulo">{{ this.noticia.titulo }}</p>
        <p v-if="this.noticia.descripcion" class="texto px-sm-5 py-sm-2 px-3 py-1">
          {{this.noticia.descripcion}}
        </p>
        <p v-if="this.noticia.miniatura && (this.noticia.miniatura.src || this.noticia.miniatura.alt)"
          class="texto px-sm-5 py-sm-2 px-3 py-1">
          <b-img
            :src="`server/api/uploads/${this.noticia['miniatura']['src']}`" fluid-grow
            :alt="`${this.noticia['miniatura']['alt']}`" class="mt-3">
          </b-img>
        </p>
        <div class="px-sm-5 py-sm-2 px-3 py-1">
          <hr class="my-4">
          <p class="text-right lead text-muted font-italic">
            <span>{{ this.printDate(this.noticia.fecha) }}</span>
          </p>
        </div>
        <ContenidoNota :contenido="this.noticia.nota"/>
        <div class="px-sm-5 py-sm-2 px-3 py-1">
          <p class="text-right lead text-muted font-italic">
            <span v-if="this.noticia.firma && this.noticia.firma != 'Anónimo'">Edición: {{ this.noticia.firma }}</span>
          </p>
        </div>
      </div>
      <div id="bannerContainer" class="my-5 col-lg-8 col-md-10 col-11 mx-auto text-justify p-0">
          <BannerAd/>
      </div>
      <RelatedNews :idNoticia="this.$route.params.id"/>
    </div>
    <Footer/>
  </div>
</template>

<script>
  import Vue from 'vue'
  import BannerAd from './bannerAd'
  import RelatedNews from './relatedNews'
  import Footer from './footer'

  function b64DecodeUnicode (str) {
    return decodeURIComponent(atob(str).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
    }).join(''))
  }

  Vue.component('ContenidoNota', {
    props: ['contenido'],
    render (h) {
      return h({
        template: '<b-jumbotron class="p-0 m-0 texto" id="jumbotronContainer">' + b64DecodeUnicode(this.contenido) + '</b-jumbotron>'
      })
    }
  })

  export default {
    name: 'theNew',
    components: { BannerAd, RelatedNews, Footer },
    data () {
      return {
        portada: '/static/portada.png',
        logo: '/static/inicio.png',
        noticia: {
          titulo: 'Cargando noticia...',
          descripcion: ' ',
          miniatura: {
            alt: ' ',
            src: ' '
          },
          nota: 'PHA+Y2FyZ2FuZG8uLi48L3A+'
        }
      }
    },
    mounted: function () {
      this.$http.get(`${this.$server}/api/noticia/` + this.$route.params.id)
          .then(response => (this.noticia = response.data.items[0]))
          .catch(error => {
            this.noticia = {
              titulo: 'Error ' + error.response.status,
              descripcion: error.response.data.items[0].error
            }
          })

       window.fbAsyncInit = () => {
          window.FB.init({
            appId: '699495854141878',
            autoLogAppEvents: true,
            xfbml: true,
            version: 'v2.7'
          })
      }
      (function (d, s, id) {
        var js
        var fjs = d.getElementsByTagName(s)[0]
        if (d.getElementById(id)) { return }
        js = d.createElement(s)
        js.id = id
        js.src = '//connect.facebook.net/en_US/sdk.js'
        fjs.parentNode.insertBefore(js, fjs)
      }(document, 'script', 'facebook-jssdk'))

      setTimeout(() => {
        this.initFB()
      }, 1000)
  },
  methods: {
    initFB () {
      window.FB.XFBML.parse()
    },
    printDate (dateString) {
      if (!dateString) return
      const dateTimeFormat = new Intl.DateTimeFormat('es-ES', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })
      let auxDate = new Date(dateString)
      let mydate = dateTimeFormat.format(new Date(auxDate.getTime() + auxDate.getTimezoneOffset() * 60000))
      return mydate.charAt(0).toUpperCase() + mydate.slice(1)
    }
  }
}

</script>

<style scoped>
  #principal {
    background-color: #ec268f;
  }

  .pTitulo {
    font-size: xxx-large;
  }

  #bannerContainer, .contenido {
    top: -80px;
  }

  #titulo, #relacionadas {
    font-family: 'Inter', sans-serif;
  }

  .texto {
    font-family: 'Roboto', sans-serif;
    font-size: x-large;
  }

  .contenido {
    background-color: white;
    height: auto !important;
    overflow: hidden !important;
  }

  #logoRegresar {
    width: 8%;
  }

  #botonRegresar {
    display: block;
    background-color: transparent;
    border-radius: 50px 50px 50px 50px;
    border-right: white 0px solid;
  }

  #jumbotronContainer {
    background-color: white;
  }

  @media only screen and (min-width: 801px) and (max-width: 992px) {
    #logoRegresar {
      width: 12%;
    }

    #bannerContainer, .contenido {
      top: -60px;
    }

    .pTitulo {
      font-size: xx-large;
    }

    .texto {
      font-size: large;
    }
  }

  @media only screen and (max-width: 800px) {
    #logoRegresar {
      width: 15%;
    }
  }

  @media only screen and (max-width: 600px) {
    #logoRegresar {
      width: 17%;
    }
  }

  @media only screen and (max-width: 575px) {
    #bannerContainer, .contenido {
      top: -40px;
    }
  }

  @media only screen and (max-width: 476px) {
    .pTitulo {
      font-size: x-large;
    }

    .texto {
      font-size: medium;
    }

    #logoRegresar {
      width: 20%;
    }
  }

</style>