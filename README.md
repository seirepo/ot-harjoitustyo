# Ostosten seurantasovellus

Sovelluksen avulla voi seurata tekemiään ostoksia ja niihin käyttämäänsä rahaa. Käyttäjä voi syöttää ohjelmaan kuitin ostoksistaan, ja ohjelmalla voi luoda yhteenvedon ostetuista tuotteista halutulla aikavälillä.

## Dokumentaatio
[Työaikakirjanpito](tyoaikakirjanpito.md)

[Vaatimusmäärittely](dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](dokumentaatio/arkkitehtuuri.md)

## Komentorivikomennot 

### Ajaminen
- komennolla `mvn compile exec:java -Dexec.mainClass=receiptapp.Main`

### Testaus
- Testit suoritetaan komennolla `mvn test`
- Testikattavuusraportti luodaan komennolla `mvn jacoco:report`
- Kattavuusraporttia voi tarkastella tiedostosta *target/site/jacoco/index.html*

### Checkstyle
- Checkstyle-raportti luodaan komennolla `mvn jxr:jxr checkstyle:checkstyle`
- Raportti löytyy */target/site/checkstyle.html*

### jar-paketin luominen
- jar-tiedosto luodaan komennolla `mvn package`
- ohjelman suoritetaan jar:na komennolla `java -jar Kuittitietokanta-1.0-SNAPSHOT.jar`

### Releaset
- [Viikon 5 versio](https://github.com/serepo/ot-harjoitustyo/releases/tag/viikko5)