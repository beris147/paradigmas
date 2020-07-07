<template>
  <div id="principal" class="parallax" >
    <CoverPage/>
    <MenuBar/>
    <Category :info="item" :pos="number" v-for="(item, number) in this.categorias" :key="number"/>
    <Footer/>
  </div>
</template>

<script>
  import CoverPage from '../components/coverPage'
  import MenuBar from '../components/menuBar'
  import ShowToday from '../components/showToday'
  import Footer from '../components/footer'
  import theNew from '../components/theNew.vue'
  import Category from '../components/category'

  export default {
    name: 'welcome',
    components: { theNew, CoverPage, MenuBar, ShowToday, Footer, Category },
    data () {
      return {
        mainProps: {src: '/static/logo.png', class: 'col-10'},
        portada: '/static/portada.png',
        logo: '/static/logo.png',
        logoMenu: '/static/logoMenu.png',
        categorias: null
      }
    },
    mounted () {
      this.$http.get('server/api/api/categorias')
          .then(response => (this.categorias = response.data.items))
          .catch(error => console.log(error))
    }
  }
</script>
