package deliberative.template.rivas;

import logist.simulation.Vehicle;
import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.*;

public class AStar {
    protected State aStarAlgorithm(Vehicle vehicle, TaskSet taskSet) {
        BFS bfs = new BFS();
        State firstNode = bfs.initState(vehicle, taskSet);

        List<State> q = new LinkedList<>();
        List<State> cost = new LinkedList<>();
        q.add(firstNode);
        do {
            State firstElement = q.get(0);
            q.remove(0);
            if (firstElement.goalState()) {
                return firstElement;
            } else {
                boolean currentAction = false;
                for (State currentState : cost) {
                    if (currentState.equals(firstElement)) {
                        currentAction = true;
                        break;
                    }
                }
                if (!currentAction) {
                    cost.add(firstElement);
                    estimationOfTheCheapestNodeToArriveAtN(q,
                            firstElement.generateSuccesors(firstElement, vehicle.capacity()));
                }
            }
        } while (!q.isEmpty());
        return null;
    }

    private static List<State> estimationOfTheCheapestNodeToArriveAtN(List<State> q, List<State> generateSuccessors) {
        q.addAll(generateSuccessors);
        Collections.sort(q, new Sort());
        return q;
    }

    private static double heuristicFunction(State s) {
        double minD = 0;
        City currentCity = s.getCurrentCity();
        Set<City> citiesToVisit = new HashSet<>();
        citiesToVisit.add(currentCity);

        Set<City> cities = new HashSet<>();
        s.getAvailableTask().stream().map(task -> task.deliveryCity).forEach(cities::add);
        s.getDeliverTo().stream().map(task -> task.pickupCity).forEach(cities::add);
        s.getDeliverTo().stream().map(task -> task.deliveryCity).forEach(cities::add);
        cities.remove(currentCity);
        if (cities.isEmpty()) {
            return 0;
        }
        while (!cities.isEmpty()) {
            double minimunDistance = Double.MAX_VALUE;
            City closestCity = null;
            for (City visitCity : citiesToVisit) {
                for (City city : cities) {
                    double distance = visitCity.distanceTo(city);
                    if (distance < minimunDistance) {
                        minimunDistance = distance;
                        closestCity = city;
                    }
                }
            }
            cities.remove(closestCity);
            citiesToVisit.add(closestCity);
            minD += minimunDistance;
        }
        return minD;

    }

    static Map<State, Double> heuristicTable = new HashMap<>();

    protected static double heuristic(State currentState) {
        if (heuristicTable.containsKey(currentState)) {
            return heuristicTable.get(currentState);
        }
        Double val = heuristicFunction(currentState);
        heuristicTable.put(currentState, val);
        return val;
    }

}

class Sort implements Comparator<State> {
    public int compare(State state1, State state2) {
        double v1 = state1.getDistanceBetweenTwoCities() + AStar.heuristic(state1);
        double v2 = state2.getDistanceBetweenTwoCities() + AStar.heuristic(state2);
        if (v1 > v2)
            return 1;
        else if (v1 < v2)
            return -1;
        else
            return 0;
    }
}
