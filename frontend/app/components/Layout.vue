<template>
    <v-app dark>
        <v-toolbar>
            <v-icon color="red">insert_chart</v-icon>
            <v-toolbar-title>CycleGAN Manager</v-toolbar-title>
        </v-toolbar>
        <v-content>
                <router-view></router-view>
        </v-content>

    </v-app>
</template>

<script>


    export default {
        name: "layout"
    }
</script>
