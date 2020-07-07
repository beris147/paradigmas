<template>
    <div v-if="this.show" class="contenido col-lg-8 col-md-10 col-11 mx-auto p-lg-4 p-md-2 px-0 py-2 m-0">
      <div class="py-0 px-3 col-12 m-0">
        <p id="relacionadas">Más para ti...</p>
        <div class="animated fadeIn p-0 m-0">
            <b-card-group deck >
                <router-link :to="{ name: 'theNew', params: {id: (value.id) ? value.id : 0 } }" class="linkNoticia col-md-4 col-8 m-md-0 mx-auto p-0"
                    v-for="(value, index) in paginas" :key="index">
                    <b-card
                        :img-src="(value['miniatura']['src'] != undefined) ? `server/api/uploads/${value['miniatura']['src']}` : ''"
                        :img-alt="(value['miniatura']['alt'] != undefined) ? value['miniatura']['alt'] : ''"
                        img-top>
                        <b-card-text>{{value['titulo']}}</b-card-text>
                    </b-card>
                </router-link>
            </b-card-group>
            <div class="card-pagination pt-5">
                <div class="page-index mx-1" v-for="i in noPaginas" :key="i"  @click="goto(i)" :class="{active:currentPage(i)}"></div>
            </div>
        </div>
      </div>
    </div>
</template>

<script>
    export default {
        name: 'RelatedNews',
        props: ['idNoticia'],
        data () {
            return {
                lista: [],
                pagina: [],
                noPaginas: 0,
                noticiasPagina: 3,
                paginaIndex: 0,
                show: true
            }
        },
        mounted: function () {
            this.$http.get(`${this.$server}/api/noticia/related/${this.idNoticia}`)
                .then(response => (this.lista = response.data.items))
                // eslint-disable-next-line
                .catch(error => { this.show = false })
        },
        computed: {
            paginas () {
                this.nuevaPagina()
                return this.pagina[this.paginaIndex]
            }
        },
        methods: {
            currentPage (i) {
                return i - 1 === this.paginaIndex
            },
            nuevaPagina () {
                let tamanioLista = Object.keys(this.lista).length
                this.noPaginas = 0
                for (let i = 0; i < tamanioLista; i = i + this.noticiasPagina) {
                    this.pagina[this.noPaginas] = this.lista.slice(i, i + this.noticiasPagina)
                    this.noPaginas++
                }
            },
            goto (i) {
                this.paginaIndex = i - 1
            }
        }
    }
</script>

<style scoped>
    .contenido {
        top: -80px;
        background-color: white;
    }

    .card-pagination {
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .page-index {
        width: 15px;
        height: 15px;
        border-radius: 15px;
        background: #ec268f;
    }

    .active {
        width: 20px;
        height: 20px;
        border-radius: 20px;
    }

    #relacionadas {
        font-size: xx-large;
    }

    @media only screen and (max-width: 992px) {
       .contenido {
            top: -60px;
        }

        #relacionadas{
            font-size: x-large;
        }
    }

    @media only screen and (max-width: 575px) {
        .contenido {
            top: -40px;
        }

        #relacionadas{
            font-size: large;
        }
    }
</style>