<template>
    <div class="col-12 mx-md-0 mx-auto order-1 order-md-3 order-sm-1 mb-3">
        <a v-bind:href="ad.href" target="_blank">
            <div>
                <b-card no-body class="overflow-hidden p-3">
                    <b-row no-gutters>
                        <b-col lg="3" md="6" sm="12">
                            <b-card-img v-bind:src="(ad.imagen) ? `${this.$server}/uploads/${ad.imagen}` : '/static/logoLetras.png'" alt="Image" class="rounded-0"></b-card-img>
                        </b-col>
                        <b-col lg="9" md="6" sm="12">
                            <b-card-body v-bind:title="ad.titulo">
                                <b-card-text>
                                    {{ad.texto}}
                                </b-card-text>
                            </b-card-body>
                        </b-col>
                    </b-row>
                </b-card>
            </div>
        </a>
    </div>
</template>

<script>
    export default {
        name: 'BannerAd',
        data () {
            return {
                ad: {
                    titulo: 'anuncio',
                    href: 'https://www.youtube.com/channel/UC5qgk9xFZhXjzvCRcZn8KqQ',
                    texto: 'Visita nuestro canal de YouTube'
                }
            }
        },
        mounted: function () {
            this.$http.get(`${this.$server}/api/anuncio/random`)
                .then(response => (this.ad = response.data.items[0]))
                // eslint-disable-next-line
                .catch(error => { 
                    this.ad = {
                        titulo: 'anuncio',
                        href: 'https://www.youtube.com/channel/UC5qgk9xFZhXjzvCRcZn8KqQ',
                        texto: 'Visita nuestro canal de YouTube'
                    }
                })
        }
    }
</script>

<style scoped>
    a {
        text-decoration: none !important;
        color: black !important;
        font-size: large;
    }

    @media only screen and (max-width: 767px) {
        .mydiv{
            text-align: center !important;
        }
    }
</style>