<template>
    <div>
        <theNew :cantidadNoticias="this.cantidadNoticias" :key="this.$route.fullPath"/>
    </div>
</template>

<script>
    import theNew from '@/components/theNew'

    export default {
        components: { theNew },
        data () {
            return {
                cantidadNoticias: ''
            }
        },
        mounted: function () {
            this.$http.get(`${this.$server}/api/noticias/total`)
                .then(response => (this.cantidadNoticias = response.data.items[0]['value']))
                .catch(error => console.log(error))
        }
    }
</script>