# Explicación del Agente Deliberativo

## Métodos Implementados

### BFS Classs

#### BFS

### AStar Class

#### AStar

### State

#### generateSuccessors

# Resultados

Los resultados son los siguientes :

#### BFS

![Image of Example 2](https://github.com/pablin2402/deliberative-Rivas/blob/master/images/bfs.png)

#### A\*

![Image of Example 3](https://github.com/pablin2402/deliberative-Rivas/blob/master/images/ASTAR.png)

## Ejecucion

Para ejecutar este proyecto basta con ejecutar el siguiente comando:

    gradlew run

on Windows, or

     ./gradlew run

on a UNIX operating system.

Los argumentos de la línea de comando puede ser cambiados en el archivo `build.gradle` :

    run.setArgsString('-a config/agents.xml config/reactive.xml reactive-random')

Nuevos agentes deben ser añadidos en el archivp`config/agents.xml`, y la participación del agente debe estar adjuntada añadiendo el nombre del agente al final de la lista de argumentos
