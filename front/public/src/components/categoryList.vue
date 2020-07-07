<template>
    <div class="col-md-9 col-12 mx-md-0 mx-auto row">
        <b-card-group deck class="mb-3" v-for="(item, number) in this.noticiasLista" :key="number">
            <router-link :to="{ name: 'theNew', params: {id: (value.id) ? value.id : 0 } }" class="linkNoticia col-6"
            v-for="value in item" :key="value.id">
            <b-card v-if="value['miniatura'] != undefined" class="tarjetaNoticia tarjetaNoticiaCat2 mb-3" style="text-decoration: none;"
                :title="value['titulo']"
                :img-src="(value['miniatura']['src'] != undefined) ? `server/api/uploads/${value['miniatura']['src']}` : ''"
                :img-alt="(value['miniatura']['alt'] != undefined) ? value['miniatura']['alt'] : ''"
                img-top>
                <b-card-text class="descripcionNoticia row p-4 m-0">
                <p class="my-auto">{{value['descripcion']}}</p>
                </b-card-text>
            </b-card>
            </router-link>
        </b-card-group>
    </div>
</template>

<script>
    export default {
        name: 'CategoryList',
        props: ['noticiasLista']
    }
</script>