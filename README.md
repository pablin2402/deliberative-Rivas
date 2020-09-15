# Explicación del Agente Deliberativo

## Métodos Implementados

### BFS Classs

#### BFS

El algoritmo BFS fue relativamente sencillo de implementar ya que se tenía experiencia con otros proyectos. Prácticamente se siguió el pseudocódigo provisto en clases y ya. Algo a notar es que cuando se configura _logist_ para proveer más de 6 paquetes el algoritmo tarda demasiado, y aquí saco la conclusión de que se generan ciclos que hacen que se visite una ciudad varias veces.

#### InitState

Básicamente lo que hace este método es inicializar el estado, añadiendo las tareas por recorrer, donde está el vehículo, y la carga que tiene el vehículo

### AStar Class

#### AStar

El algoritmo A\* se parece bastante al BFS, por no decir que es casi el mismo la diferencia está en la función heurística utilizada,
siguiendo:
f(n) = g(n) + h(n)
donde g(n) es la distancia para llegar a un nodo y h es la heurística menos el costo desde ese nodo hasta la meta.
Esto hace que se visite a los estados con la menor distancia posible.

La heurística que se implementó ve la ciudad actual y crea una colección SET con con las ciudades de entrega y recogida, ve la menor distancia entre la ciudad actual a la vecina y almacena la de menor
costo. Devolviendo la distancia mínima entre una ciudad que tiene una tarea por recoger o entregar. Lo que hace que no vaya envano a una ciudad que no corresponda y mejorando el rendimiento de está, pudiendo con más de 15 paquetes
con un procesador intel core I7 de Octava Generación.

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

Donde se define en **currentCity** la ciudad donde el vehículo está, **distanceBetweenTwoCities**la distancia entre la ciudad actual y otra ciudad vecina, **currentActions** que almacena las acciones como ser _(MOVE, PICKUP y DELIVER)_, **availableTask** es una lista donde se almacenan los paquetes que todavía no han sido recogidos y **deliverTo** es otra lista que almacena los paquetes que ya se recogieron y están por ser entregados.

#### generateSuccessors

Este método genera los hijos del estado actual, recibe como parámetros al estado y la capacidad de kilogramos que el automóvil puede llevar.
Genera los estados a los que el estado actual pueda acceder. Lo que se hace es que si el auto está en una ciudad donde se debe entregar un paquete que tiene el carro, este lo entrega
y verifica si existe un paquete para recoger, este recogerá todos los paquetes que el auto pueda llevar, entonces habrán ocasiones en las que se dejarán algunos paquetes.
Para moverse a recoger dicho paquete jugamos con un boolean, con este se controla si se recogió o no, en caso de que si, se va a la ubicación correspondiente.
Para que exista modularidad y el método no sea muy extenso se crearon 3 métodos adicionales: **MOVE, DELIVER y PICK UP**. Lo que hace **MOVE** es añadir la opción de moverse a la lista de acciones y se actualiza el estado.
**DELIVER** juega con dos listas: acciones y deliverto, añade la acción de deliver y elimina la tarea de deliverto porque está ya se entrego.**PICK UP** juega con las tres listas, elimina la tarea 
de la lista de tareas disponibles por recoger, añade la tarea a la lista de deliverto y también añade la acción de Pick Up a la lista de acciones y actualiza el estado.  

##### remainingCapacity

Lo que hace este método es bastante simple, recorre la lista de **deliverTo** sumando la carga de cada paquete, esto con el fin de no exceder con el peso que el auto puede cargar.

#### goalState

Verifica que las listas **deliverTo** y **availableTask** queden  vacías, en el caso que si devuelve true. Este método se usa en el algoritmo BFS y A\*.

# Resultados

Los resultados son los siguientes :

#### BFS

Número de paquetes 5, BFS vs Agente Aleatorio

![Image of Example 2](https://github.com/pablin2402/deliberative-Rivas/blob/master/images/bfs.png)

#### A\*

Número de paquetes 15, A\* vs Agente Aleatorio
![Image of Example 3](https://github.com/pablin2402/deliberative-Rivas/blob/master/images/ASTAR.png)

## Conclusión 
En conclusión el A* trabaja mucho mejor que el BFS ya que el BFS se limita a ir al primer estado que se encuentra mientras el ASTAR que es muy similar sigue
la heurística implementada que dependiendo si está bien diseñada se pueden recoger una infinidad de paquetes.Con la heurística implementa filtra una lista con las tareas por recoger y las tareas por entregar y ve la menor distancia.
Con una computadora con un procesador INTEL CORE I7 de 8va generación y 16 gb de RAM fácilmente se puede hacer BFS para 6 paquetes. En la siguiente tabla se puede apreciar el tiempo de ejecución, reward per km (recompensa final, no la más alta).

![Image of Example 4](https://github.com/pablin2402/deliberative-Rivas/blob/master/images/COMPARACION.png)

Se ejecutaron simulaciones con 3 agentes deliberativos  A*
dando los siguientes resultados:

![Image of Example 5](https://github.com/pablin2402/deliberative-Rivas/blob/master/images/3ASTAR.png)

Se pueden ver que dos agentes terminaron casi empatando y otro que no tuvo el mejor rendimiento, los tres fueron situados en ciudades aledañas, es decir una alado de la otra, dos de ellos en la misma ciudad, lo que me da a pensar es que los dos que estaban en la misma ciudad 
se peleaban por los paquetes ya que empezaban con el mismo plan así que dependiendo quien llegaba primero (aquí llamo a la suerte por así decirlo) conseguía los paquetes y perjudicaba al otro auto que practicamente estaba alado, lo que seriamente lo perjudico al principio ya que tenía que 
re calcular la mejor opción. El tercero recogía la _sobra_ por así decirlo, suena mal pero no es así ya que aunque tardando más, porque tenía que re calcular la estrategia no era perjudicado porque otro auto lo deje sin paquete por nada de conseguir este, simplemente calculaba su mejor opción 
y llegaba sin problemas a ella(a menos que otro auto llegue primero, cosa que puede pasar y que pasa).
## Ejecución

Para ejecutar este proyecto basta con ejecutar el siguiente comando:

    gradlew run

on Windows, or

     ./gradlew run

on a UNIX operating system.

Los argumentos de la línea de comando puede ser cambiados en el archivo `build.gradle` :

    run.setArgsString('-a config/agents.xml config/reactive.xml reactive-random')

Nuevos agentes deben ser añadidos en el archivp`config/agents.xml`, y la participación del agente debe estar adjuntada añadiendo el nombre del agente al final de la lista de argumentos
