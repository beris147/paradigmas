<template>
    <div class="col-md-3 col-12 mx-md-0 mx-auto stickyAd divFlotante order-2 order-md-1 order-sm-2">
        <a v-bind:href="ad.href" target="_blank">
            <div>
                <b-card
                    v-bind:title="ad.titulo"
                    v-bind:img-src="(ad.imagen) ? `${this.$server}/uploads/${ad.imagen}` : '/static/logoLetras.png'"
                    img-alt="Image"
                    img-top
                    tag="article"
                    class="mb-2 p-3">
                    <b-card-text>
                    {{ad.texto}}
                    </b-card-text>
                </b-card>
        </div>
        </a>
    </div>
</template>

<script>
    export default {
        name: 'FloatAd',
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
        font-size: large !important;
    }

    .stickyAd {
        position: sticky !important;
        top: 1.5em !important;
        z-index: 1000 !important;
    }

    @media only screen and (max-width: 767px) {
        .stickyAd {
            position: relative !important;
            top: 1.5em !important;
            z-index: 1000 !important;
        }
    }
</style>