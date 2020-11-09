# Vaatimusmäärittely

## Sovelluksen tarkoitus
Sovelluksen avulla voi seurata tekemiään ostoksia ja niihin käyttämäänsä rahamäärää.

## Käyttöliittymäluonnos
Sovellus koostuu perusnäkymästä, johon liittyy kaksi näkymää: kaikki kuitit sisältävä näkymä ja yhteenvetonäkymä.

Perusnäkymässä on listattuna ostokset laskevassa aikajärjestyksessä, ja ostoksista näkyy päivämäärä, myymälä ja loppusumma. Perusnäkymän pääikkuna on jaettu kahteen osaan, listausnäkymään ja esikatselunäkymään, jossa voidaan tarkastella ja muokata kuitin sisältöä. Tässä samassa näkymässä hoituu myös kuitin lisäys. Perusnäkymän ostoslistauksessa on mahdollisuus järjestää ostokset myös summan mukaan tai rajata kuitteja päivämäärällä.

Yhteenvetonäkymässä on oletuksena viimeisen viikon aikana tehtyjen ostosten määrä ja kokonaissumma ja muuta yhteenvetoa ostoksista.

## Ominaisuudet

- päänäkymä, jossa lisätyt ostokset ovat aikajärjestyksessä
- mahdollisuus lisätä järjestelmään uuden kuitin
    - kuitissa täytyy olla myymälä, päivämäärä, loppusumma ja vähintään yksi tuote. määrä ja kellonaika on vapaaehtoinen
- mahdollisuus valita päänäkymästä esikatseltavaksi kuitti ja tarvittaessa muokata sitä
- aikarajauksella voi tarkastella esimerkiksi kuluneen viikon aikana tehtyjä ostoksia
- yhteenvetovälilehti kaikista ostoksista:
    - käytetty rahamäärä (tietyllä aikavälillä jota voi muokata)
    - yleisin myymälä
    - ostettujen tuotteiden määrän keskiarvo
    - ostetuimmat tuotteet top 5, niihin käytetty raha ja kuinka monelta kuitilta ne löytyvät
- kuitin voi poistaa

## Jatkokehitysideoita

Jos aikaa riitää, voidaan ohjelmaa täydentää seuraavilla ominaisuuksilla:
- ostoksia voi tarkastella myös tuotteittain
    - tällöin tuotteet aakkosjärjestyksessä
- tähän voitaisiin lisätä myös rajaus haulla, eli kuinka monelta kuitilta löytyy nuudeleita
- kuitille voi lisätä maksajan
