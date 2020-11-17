# Vaatimusmäärittely: ostosten seurantasovellus

## Sovelluksen tarkoitus ja käyttö
Sovelluksen avulla voi seurata tekemiään ostoksia ja niihin käyttämäänsä rahamäärää. Sovellukseen voidaan hakea tiedot olemassaolevasta tietokannasta ja jatkaa sen muokkausta, tai aloittaa kokonaan uusi ostosseuranta. Tiedot voidaan tallentaa, tai sovellus voidaan sulkea tallentamatta.

## Käyttöliittymäluonnos
Sovellukseen liittyy kaksi välilehtimäistä näkymää: kuittinäkymä ja yhteenvetonäkymä.

Kuittinäkymä on jaettu kahteen osaan: listausnäkymään, jossa ostokset ovat laskevassa aikajärjestyksessä taulukossa, jonka otsikkorivillä on myymälä, päivämäärä ja loppusumma, ja esikatselunäkymään, jossa voidaan tarkastella ja muokata kuitin sisältöä. Esikatselunäkymässä hoituu myös uuden kuitin lisäys. Kuittinäkymässä on mahdollisuus järjestää kuitit myös summan mukaan tai rajata kuitteja päivämäärällä.
Yhteenvetonäkymässä on oletuksena viimeisen viikon aikana tehtyjen ostosten määrä, kokonaissumma ja muita tietoja ostoksista sekä niihin liittyvistä tuotteista.

## Toiminnallisuudet

### Listausnäkymä
- mahdollisuus lisätä järjestelmään uusi kuitti
    - kuitissa täytyy olla myymälä, päivämäärä ja vähintään yksi rivi (tuote), jossa määrä on vähintään 1        
- mahdollisuus valita päänäkymästä esikatseltavaksi kuitti ja tarvittaessa muokata sitä
- aikarajauksella voi tarkastella esimerkiksi kuluneen viikon aikana tehtyjä ostoksia
- kuitin voi poistaa
- tuotteiden yksinkertainen tarkastelu, listausnäkymään mahdollisuus valita myös tuotteet

### Yhteenvetonäkymä
- oletuksena ei aikarajausta, mutta voidaan muokata

#### Ostokset
- käytetty rahamäärä
- yleisin myymälä
- ostettujen tuotteiden määrän keskiarvo

#### Tuotteet
- ostetuimmat tuotteet top 5, niihin käytetty raha ja kuinka monelta kuitilta ne löytyvät
- ostettujen tuotteiden kokonaismäärä ja uniikkien tuotteiden kokonaismäärä

## Jatkokehitysideoita
Jos aikaa riitää, voidaan ohjelmaa täydentää seuraavilla ominaisuuksilla:
- ostoksia voi tarkastella myös tuotteittain
    - tällöin tuotteet aakkosjärjestyksessä
- tuotteelle voidaan valita myös paino (kg) tai tilavuus (l)
- yhteenvetonäkymään mahdollisuus tarkastella myös haluttua tuotetta hakemalla, eli kuinka monelta kuitilta löytyy nuudeleita
- yhteenvetonäkymässä muutos edelliseen viikkoon (tai muuhun ajanjaksoon)
- tuotekategoriat
