# Ostosten seurantasovellus

Sovelluksen avulla voi seurata tekemiään ostoksia ja niihin käyttämäänsä rahaa. Käyttäjä voi syöttää ohjelmaan kuitin ostoksistaan, ja ohjelmalla voi luoda yhteenvedon ostetuista tuotteista halutulla aikavälillä.

## Dokumentaatio
[työaikakirjanpito](tyoaikakirjanpito.md)

[vaatimusmäärittely](dokumentaatio/vaatimusmaarittely.md)

## Komentorivikomennot 

### Testaus
Testit suoritetaan komennolla `mvn test`
Testikattavuusraportti luodaan komennolla `mvn jacoco:report`
Kattavuusraporttia voi tarkastella tiedostosta *target/site/jacoco/index.html*

### Checkstyle
Checkstyle-raportti luodaan komennolla `mvn jxr:jxr checkstyle:checkstyle`
Raportti löytyy */target/site/checkstyle.html*
