# Explicación del Agente Deliberativo

## Métodos Implementados

### BFS Classs

#### BFS

El algoritmo BFS fue relativamente sencillo de implementar ya que se tenía experiencia con otros proyectos. Practicamente se siguio el pseudocódigo provisto en clases y ya. Algo a notar es que cuando se configura logist para proveer más de 6 paquetes el algoritmo tarda demasiado, y aquí saco la conclusión de que se generan ciclos que hacen que se visite una ciudad varias veces.

#### InitState

Basicamente lo que hace este método es inicializar el estado, añadiendo las tareas por recorrer, donde está el vehículo, y la carga que tiene el vehículo

### AStar Class

#### AStar

El algoritmo A\* se parece bastante al BFS, por no decir que es casi el mismo la diferencia está en la función heurística utilizada,
siguiendo:
f(n) = g(n) + h(n)
donde g(n) es la distancia para llegar a un nodo y h es la heurística menos el costo desde ese nodo hasta la meta.
Esto hace que se visite a los estados con la menor distancia posible.

### State

Los atributos definidos en estados son :

```java
    private City currentCity;
    private double distanceBetweenTwoCities;
    // currentActions
    private List<Action> currentActions;
    // aceptadas
    private List<Task> availableTask;
    // a ser entregadas
    private List<Task> deliverTo;
```

donde se define la ciudad actual que es donde el vehículo esta, la distancia entre la ciudad actual y otra ciudad vecina, currentactions que almacena las acciones, availableTask son los paquetes que todavía no han sido recogidos y deliverTo son los paquetes que ya se recogieron y están por ser entregados.

#### generateSuccessors

# Resultados

Los resultados son los siguientes :

#### BFS

Número de paquetes 5, BFS vs Agente Aleatorio

![Image of Example 2](https://github.com/pablin2402/deliberative-Rivas/blob/master/images/bfs.png)

#### A\*

Número de paquetes 15, A\* vs Agente Aleatorio
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
