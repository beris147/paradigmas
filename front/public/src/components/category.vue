<template>
    <div v-bind:style="background" class="p-xl-5 p-lg-3 p-1">
      <p class="tituloSeccion h1 p-md-5 p-3 mt-md-0 mt-4">{{this.info.nombre | capitalize }}</p>
      <div v-if="this.error" class="text-center">
          <h3>Error {{this.error.status}}</h3>
          <h5>{{this.error.message}}</h5>
        </div>
      <div v-if="this.pos !== 0" class="row mx-md-5 mx-1 my-md-2 my-1 pb-5">
        <CategoryList :noticiasLista="this.noticiasLista"/>
        <FloatAd/>
        <BannerAd class="col-md-9"/>
        <CategoryList class="order-3 mt-5" :noticiasLista="this.noticiasExtra" :key="this.pagina"/>
      </div>
      <div v-if="this.pos !== 0 && this.more" class="col-12 row m-0 p-0" id="seeMoreContainer">
        <button class="mx-auto" id="seeMoreButton" v-on:click="seeMore">Ver más</button>
      </div>
      <div v-if="this.pos === 0">
          <ShowToday :categoria="this.info.id"/>
      </div>
    </div>
</template>

<script>
    import FloatAd from '../components/floatAd.vue'
    import BannerAd from '../components/bannerAd.vue'
    import CategoryList from '../components/categoryList'
    import ShowToday from '../components/showToday'

    export default {
        name: 'Category',
        props: ['info', 'pos'],
        data () {
            return {
                logo: '/static/logo.png',
                noticiasLista: [
                  {
                    id: 0,
                    miniatura: { alt: 'cargando' }
                  }
                ],
                pagina: 1,
                limite: (this.pos === 0) ? 1 : 6,
                noticiasExtra: [],
                background: {
                    background: '#' + this.info.color
                },
                more: true,
                error: null
            }
        },
        components: {
          FloatAd,
          BannerAd,
          CategoryList,
          ShowToday
        },
        mounted: function () {
            this.$http.get(`server/api/api/noticias/categoria/${this.info.id}/pagina=1/limite=${this.limite}`)
                .then(response => (this.noticiasLista = [response.data.items]))
                // eslint-disable-next-line
                .catch(error => {
                    this.error = {
                      status: error.response.status,
                      message: error.response.data.items[0].error
                    }
                })
            this.$http.get(`server/api/api/noticias/categoria/${this.info.id}/pagina=${++this.pagina}/limite=${this.limite}`)
                .then(response => (this.noticiasExtra[this.pagina - 2] = response.data.items))
                // eslint-disable-next-line
                .catch(error => {this.more = false})
        },
        methods: {
            seeMore: function () {
              this.$http.get(`server/api/api/noticias/categoria/${this.info.id}/pagina=${++this.pagina}/limite=${this.limite}`)
                .then(response => (this.noticiasExtra[this.pagina - 2] = response.data.items))
                // eslint-disable-next-line
                .catch(error => {this.more = false})
            }
        },
        filters: {
            capitalize: function (value) {
                if (!value) return ''
                value = value.toString()
                return value.charAt(0).toUpperCase() + value.slice(1)
            }
        }
    }
</script>

<style scoped>
  .tarjetaNoticiaCat2 img {
    height: 250px !important;
  }

  #seeMoreButton {
    background-color: transparent;
    border: none;
    font-family: 'Roboto', sans-serif;
    font-size: x-large;
    color: white;
  }
</style>