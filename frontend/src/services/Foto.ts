
/*
 * Beispiel "Kommentar"
{
    "id": 2,
    "autor": "ab",
    "zeitpunkt": "2021-06-08T21:42:37.399333",
    "text": "Wat soll dat denn sein. So viel Grünzeuchs."
  }
*/

export interface Kommentar {
    id: number;
    autor: string;
    zeitpunkt: string;
    text: string;
}

/*
 * Beispiel "Foto"
{
    "id": 1,
    "mimetype": "image/jpeg",
    "dateiname": "bild-17.jpg",
    "ort": "New Place, Chapel Lane, Stratford-upon-Avon, Stratford-on-Avon, Warwickshire, West Midlands, England, CV37 6BE, United Kingdom",
    "zeitstempel": "2018-07-08T12:52:02",
    "geolaenge": -1.7075687499999999,
    "geobreite": 52.19060158333333,
    "kommentare": []
  }
*/

export interface Foto {
    id: number;
    mimetype: string;
    dateiname: string;
    ort: string;
    zeitstempel: string;
    geolaenge: number;
    geobreite: number;
    kommentare: number[];
}