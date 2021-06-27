module.exports = {
    devServer: {
        // setzt voraus, dass Spring Backend per application.properties auf server.port=9090 laeuft
            proxy: {
            '^/(api|foto)': { target: 'http://localhost:9090/', ws: true, secure: false },
            }
        }
    }