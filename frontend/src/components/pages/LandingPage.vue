<template>
    <div>
        <div class="custom-parallax"
             style="background-image: url('public/noise.jpg'); height: 62vh">
            <v-jumbotron >
                <v-container fill-height>
                    <v-layout align-center>
                        <v-flex>
                            <h3 class="display-3">Kill the noise</h3>
                            <v-divider class="my-3"></v-divider>
                            <div class="title mb-3">Start getting real results, in minutes.</div>
                            <v-btn large color="primary" class="mx-0">Get Started</v-btn>
                        </v-flex>
                    </v-layout>
                </v-container>
            </v-jumbotron>
        </div>
        <v-container style="height: 38vh" >
            <v-layout row wrap>
                <v-flex md4>
                    <h1>Bring your own cloud</h1>
                    <p style="font-size: 14pt">
                        You bring the compute, we'll handle the rest. Our software will utilize
                        infrastructure from popular cloud providers including Paperspace, Amazon Web Services,
                        and Nvidia's GPU Cloud.
                    </p>
                </v-flex>
                <v-flex md2></v-flex>
                <v-flex md2>
                    <a href="https://www.paperspace.com/&R=LIISII">
                        <img src="public/paperspace.png"
                             style="width: 100%; display: block; margin: auto">
                    </a>
                </v-flex>
                <v-flex md2>
                    <a href="https://aws.amazon.com/">
                        <img src="public/amazon.png"
                             style="width: 100%; display: block; margin: auto">
                    </a>
                </v-flex>
                <v-flex md2>
                    <a href="https://www.nvidia.com/en-us/gpu-cloud/">
                        <img src="public/nvidia.png"
                             style="width: 100%; display: block; margin: auto;">
                    </a>
                </v-flex>
            </v-layout>
        </v-container>
        <div class="custom-parallax"
             style="background-image: url('public/full.jpg'); height: 62vh">
            <v-jumbotron >
                <v-container fill-height>
                    <v-layout align-center>
                        <v-flex>
                            <h3 class="display-3">Get <strong>quality</strong> results</h3>
                        </v-flex>
                    </v-layout>
                </v-container>
            </v-jumbotron>        </div>
        <v-container>
            <v-layout column align-center justify-center style="height: 38vh">
                <v-flex md3></v-flex>
                <v-flex md6>
                    <h1>Oh, and the best part - It's free. </h1>
                    <br/>
                    <center>
                        <v-btn color="primary">Get Started</v-btn>
                    </center>
                </v-flex>
            </v-layout>
        </v-container>
    </div>
</template>

<script>

    export default {
        name: "landing-page"
    }
</script>
