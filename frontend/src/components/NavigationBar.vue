<template>
    <v-toolbar style="height: 7vh; position: fixed; z-index: 100">
        <v-toolbar-title>
            <v-icon>timeline</v-icon>
            <span>Gradman</span>
        </v-toolbar-title>
        <v-spacer></v-spacer>
        <v-layout row align-center style="max-width: 150px">
           <v-btn color="primary">Sign Up</v-btn>
            <a href="#">log in</a>
        </v-layout>
    </v-toolbar>
</template>

<script>
    export default {
        name: "navigationbar"
    }
</script>

