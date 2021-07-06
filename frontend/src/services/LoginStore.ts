import { reactive, readonly } from "vue";
import { Foto } from './Foto';
import { Client, Message } from '@stomp/stompjs';
import { FotoMessage } from "./FotoMesssage";

const loginstate = reactive({
    username: String(""),
    jwttoken: String(""),
    errormessage: String(""),
    isLoggedIn: Boolean(false)
})

// Base64-Codierung von 'username:password' für Basic Auth.
const credetials = btoa('${this.username}:${this.password}');

export function useLoginStore(){
    
    function doLogout() {
        loginstate.username = ("");
        loginstate.jwttoken = ("");
        loginstate.errormessage = ("");
        loginstate.isLoggedIn = false;
        console.log("Logout erfolgreich")
    }

    async function doLogin(username : string, password : string) : Promise<boolean>  {
        const userobj = { name : username , password : password};

        // URL-Pfad auf selbem Server, von dem der Frontend-Code kam
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: {
                'Authorization' : 'Basic ' + credetials,
                'Content-Type' : 'application/json',
            },
            // body: POST Request-Body, dazu
            // JSON-Objekt in String-Darstellung umformen
            body: JSON.stringify(userobj),
        })

        const jsondata = await response.json()

        if (response.ok) {
            loginstate.username = username;
            loginstate.jwttoken = jsondata;
            loginstate.errormessage = ("");
            loginstate.isLoggedIn = true;
            console.log("Login erfolgreich")
        } else {
            doLogout();
            loginstate.errormessage = ("Login fehlgeschlagen");
            console.log(loginstate.errormessage)
        }

        return loginstate.isLoggedIn;
    }
  
    return {
        loginstate: readonly(loginstate), doLogout, doLogin
    }
}