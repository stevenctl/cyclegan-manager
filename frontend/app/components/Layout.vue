<template>
    <v-app dark>
        <navigation-bar></navigation-bar>
        <v-content>
                <router-view></router-view>
        </v-content>

    </v-app>
</template>

<script>
    import NavigationBar from './NavigationBar'

    export default {
        name: "layout",
        components: {NavigationBar}
    }
</script>
