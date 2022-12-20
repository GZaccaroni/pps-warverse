<h1 align="center" style="border-bottom: none;">⚔️ warverse</h1>
<h3 align="center">Battle simulator</h3>

[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=GZaccaroni_pps-warverse&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=GZaccaroni_pps-warverse)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=GZaccaroni_pps-warverse&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=GZaccaroni_pps-warverse)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=GZaccaroni_pps-warverse&metric=bugs)](https://sonarcloud.io/summary/new_code?id=GZaccaroni_pps-warverse) \
![Build Workflow](https://github.com/GZaccaroni/pps-warverse/actions/workflows/release_please.yml/badge.svg)

Warverse è un simulatore di guerra tra più stati scritto in Scala.
È possibile definire: gli stati che partecipano al conflitto, la loro popolazione, le unità dell'esercito, le relazioni tra loro e le risorse disponibili. 
Una volta avviata la simulazione viene aggiornata con frequenza giornaliera finché non si risolvono tutte le guerre.

## Utilizzo
Una volta scaricata l'ultima versione di Warverse [qui](https://github.com/GZaccaroni/pps-warverse/releases/latest) è possibile avviarlo con il comando (da eseguire via terminale):
```
java -jar warverse.jar
```
![jar-gif](https://user-images.githubusercontent.com/71103219/208410987-edd44e51-6852-46e7-9533-ffabf12f033d.gif)



Avviata la simulazione e caricato un file json di configurazione ([qui](https://github.com/GZaccaroni/pps-warverse/releases/latest) trovi alcuni esempi) sarà possibile far andare il simulatore!

![simulation-gif](https://user-images.githubusercontent.com/71103219/208410774-bcd59ab6-9185-4873-86c2-0dcd0fee729b.gif)
