<template>
    <div id="showTodayContainer" class="p-xl-5 p-lg-3 p-1">
    <Todayshow :categoria="this.categoria"/>
    </div>
</template>

<script>
    import Vue from 'vue'
    Vue.component('Todayshow', {
        props: ['categoria'],
        data () {
            return {
              contenido: 'Cargando...'
            }
        },
        render (h) {
            return h({
              template: '<b-jumbotron class="p-0 m-0" id="jumbotronContainer" style="background-color: #1ebfd1;">' + this.contenido + '</b-jumbotron>'
            })
        },
        mounted: function () {
            this.$http.get(`${this.$server}/api/noticias/categoria/${this.categoria}/pagina=1/limite=1`)
                .then(response => (this.contenido = window.atob([response.data.items[0]['nota']])))
                .catch(error => {
                    this.contenido = '<h3>' + error.response.data.items[0].error + '</h3>'
                })
        }
    })
    export default {
        name: 'ShowToday',
        props: ['categoria']
    }
</script>

<style scoped>
    #showTodayContainer {
        height: auto;
    }
</style>