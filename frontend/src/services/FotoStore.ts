import { reactive, readonly } from "vue";
import { Foto } from './Foto';
import { Client, Message } from '@stomp/stompjs';
import { FotoMessage } from "./FotoMesssage";

const fotostate = reactive({
    fotos: Array<Foto>(),
    errormessage: String("")
})
const wsurl = "ws://localhost:9090/messagebroker";
const DEST = "/topic/foto";
const stompclient = new Client({ brokerURL: wsurl })

export function useFotoStore(){
    stompclient.onConnect = (frame) => {
    console.log("onConnect")
    // Callback: erfolgreicher Verbindugsaufbau zu Broker
    stompclient.subscribe(DEST, (message) => {
    // Callback: Nachricht auf DEST empfangen
    // empfangene Nutzdaten in message.body abrufbar,
    // ggf. mit JSON.parse(message.body) zu JS konvertieren
        const parsedMessage = JSON.parse(message.body) as FotoMessage
        console.log("Nachricht eingegangen", message.body)
        console.log("Nachricht", parsedMessage.operation)
        if (parsedMessage.operation == "fotoGespeichert" || parsedMessage.operation == "fotoGeloescht") {
             console.log("Foto geupdatet")
             updateFotos()
        }
    });
    };
    stompclient.onDisconnect = () => { console.log("Verbindung abgebaut") /* Verbindung abgebaut*/ }
    stompclient.onStompError = () => { console.log("Stomp Error") /* Verbindung abgebaut*/ }
    stompclient.onWebSocketClose = () => { console.log("WebSocket") /* Verbindung abgebaut*/ }
    // Verbindung zum Broker aufbauen
    stompclient.activate();

    async function deleteFotos(id: number) {
        fetch('api/foto/'+id, { method: 'DELETE'}
        ).then( (response) => { if (!response.ok) {
            fotostate.errormessage = "Löschen fehlgeschlagen";
            throw new Error('schade'); 
            }
            return response.json(); 
        }
        ).then(jsondata =>{
            fotostate.fotos = jsondata
        }).catch(fehler => {
            fotostate.errormessage = 'Das hat nicht funktioniert: ' + fehler})
    }

    async function updateFotos() {
            fetch('/api/foto', {
                method: 'GET',
                headers: {
            },  
        })
        .then( (response) => {
            if (!response.ok) {
                fotostate.errormessage = "Update fehlgeschlagen";
            throw new Error('schade');
        }
            // empfangene Payload -> JSON
            return response.json();
        })
        .then( (jsondata) => {
        fotostate.fotos = jsondata
        console.log("Fotos", fotostate.fotos)

        })
        .catch( (fehler) => {
            fotostate.errormessage = 'DU bist ein Fehler: {$fehler}'})
    }

    return {
        fotostate: readonly(fotostate), updateFotos, deleteFotos
    }
}