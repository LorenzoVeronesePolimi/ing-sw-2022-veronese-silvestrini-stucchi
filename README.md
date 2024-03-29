# Prova Finale di Ingegneria del Software - AA 2021-2022

Implementazione del gioco da tavolo Eriantys.


## Componenti del gruppo
- __Giada Silvestrini__
- __William Stucchi__
- __Lorenzo Veronese__

## Documentazione

### UML
Nella cartella "deliverables" sono contenuti i diagrammi UML che rappresentano la struttura del nostro progetto nelle fasi [iniziali](https://github.com/LorenzoVeronesePolimi/ing-sw-2022-veronese-silvestrini-stucchi/blob/master/deliverables/UML1March/VeroneseSilvestriniStucchiIniziale.svg),
[intermedie](https://github.com/LorenzoVeronesePolimi/ing-sw-2022-veronese-silvestrini-stucchi/tree/master/deliverables/UML2April), e [finali](https://github.com/LorenzoVeronesePolimi/ing-sw-2022-veronese-silvestrini-stucchi/tree/master/deliverables/Final).
### Coverage report
| Package | Classe | Coverage metodi | Coverage righe |
|:-----------------------|:------------------|:------------------|:------------------------------------:|
| Controller | Controller | 60/60 (100%) | 690/764 (90.3%)
| Controller | ControllerInput | 11/11 (100%) | 27/27 (100%)
| Controller | ControllerIntegrity | 21/21 (100%) | 75/75 (100%)
| Controller | ControllerState | 5/5 (100%) | 15/15 (100%)
| Model |  | 301/302 (99.7%) | 1228/1270 (96.7%)

## Funzionalità Sviluppate
| Funzionalitá | Implementazione |
|:-----------------------|:------------------------------------:|
| Regole Complete | ✅ |
| CLI | ✅ |
| GUI | ✅ |
| Socket | ✅ |
| Carte Personaggio (12) | ✅ |
| Partita 4 giocatori | ✅ |
| Persistenza | ✅ |
| Partite multiple | ⛔ |
| Resilienza alle disconnessioni | ⛔ |

#### Legenda
⛔ Non Implementata &nbsp;&nbsp;&nbsp;&nbsp; ✅ Implementata

## Jars
I jar sono stati realizzati con l'ausilio di Maven Shade Plugin, e si trovano [qui](https://github.com/LorenzoVeronesePolimi/ing-sw-2022-veronese-silvestrini-stucchi/tree/master/deliverables/jars).
Per eseguire i jar, recarsi attraverso un terminale nella cartella dove sono salvati, ed eseguire i comandi:
```
java -jar ServerApp.jar
```
```
java -jar ClientApp.jar
```
