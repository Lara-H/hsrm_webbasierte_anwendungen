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
        const userobj = { username : username , password : password};
        console.log(JSON.stringify(userobj))

        // URL-Pfad auf selbem Server, von dem der Frontend-Code kam
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type' : 'application/json',
            },
            // body: POST Request-Body, dazu
            // JSON-Objekt in String-Darstellung umformen
            body: JSON.stringify(userobj),
        })
        console.log(response.statusText)
        if (response.ok) {
            const responseText = await response.text();
            loginstate.username = username;
            loginstate.jwttoken = responseText;
            loginstate.errormessage = ("");
            loginstate.isLoggedIn = true;
            console.log("Login erfolgreich" + JSON.stringify(loginstate))
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