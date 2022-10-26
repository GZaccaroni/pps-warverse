<h1 align="center" style="border-bottom: none;">⚔️ warverse</h1>
<h3 align="center">Battle simulator</h3>
![Build Workflow](https://github.com/GZaccaroni/pps-warverse/actions/workflows/scala_build.yml/badge.svg)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=GZaccaroni_pps-warverse&metric=coverage)](https://sonarcloud.io/summary/new_code?id=GZaccaroni_pps-warverse) \
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=GZaccaroni_pps-warverse&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=GZaccaroni_pps-warverse) \
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=GZaccaroni_pps-warverse&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=GZaccaroni_pps-warverse) \
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=GZaccaroni_pps-warverse&metric=bugs)](https://sonarcloud.io/summary/new_code?id=GZaccaroni_pps-warverse) \


Warverse è un simulatore di guerra tra più stati scritto in Scala.
È possibile definire: gli stati che partecipano al conflitto, la loro popolazione, le unità dell'esercito, le relazioni tra loro e le risorse disponibili. 
Una volta avviata la simulazione viene aggiornata con frequenza giornaliera finché non rimane solo uno stato.

## Utilizzo
Una volta scaricata l'ultima versione di Warverse [qui](https://github.com/VirusSpreadSimulator/PPS-22-virsim/releases/latest) è possibile avviarlo con il comando (da eseguire via terminale):
```
java -jar warverse.jar
```