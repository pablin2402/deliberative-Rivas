package deliberative.template.rivas;

import logist.agent.Agent;
import logist.behavior.DeliberativeBehavior;
import logist.plan.Action;
import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskDistribution;
import logist.task.TaskSet;
import logist.topology.Topology;

import java.sql.ClientInfoStatus;
import java.util.List;

public class RivasDeliberative implements DeliberativeBehavior {
    enum Algorithm { BFS, ASTAR }

    /* Environment */
    Topology topology;
    TaskDistribution td;

    /* the properties of the agent */
    Agent agent;
    int capacity;

    /* the planning class */
    Algorithm algorithm;

    @Override
    public void setup(Topology topology, TaskDistribution td, Agent agent) {
        this.topology = topology;
        this.td = td;
        this.agent = agent;

        // initialize the planner
        int capacity = agent.vehicles().get(0).capacity();
        String algorithmName = agent.readProperty("algorithm", String.class, "ASTAR");

        // Throws IllegalArgumentException if algorithm is unknown
        algorithm = Algorithm.valueOf(algorithmName.toUpperCase());

        // ...
    }

    @Override
    public Plan plan(Vehicle vehicle, TaskSet tasks) {
        State currentState = new State();
        State s ;
        BFS bfs = new BFS();
        switch (algorithm) {
            case ASTAR:
                // ...
                //plan = naivePlan(vehicle, tasks);
                break;
            case BFS:
                // ...
                s = bfs.getBreathFirstSearch(vehicle, tasks);
                break;
            default:
                throw new AssertionError("Should not happen.");
        }
        List<Action> actions = currentState.getCurrentActions();
        Plan plan = new Plan(vehicle.getCurrentCity(), actions);

        return plan;
    }

    private Plan naivePlan(Vehicle vehicle, TaskSet tasks) {
        Topology.City current = vehicle.getCurrentCity();
        Plan plan = new Plan(current);

        for (Task task : tasks) {
            // move: current city => pickup location
            for (Topology.City city : current.pathTo(task.pickupCity))
                plan.appendMove(city);


            plan.appendPickup(task);

            // move: pickup location => delivery location
            for (Topology.City city : task.path())
                plan.appendMove(city);

            plan.appendDelivery(task);

            // set current city
            current = task.deliveryCity;
        }

        return plan;
    }

    @Override
    public void planCancelled(TaskSet carriedTasks) {

        if (!carriedTasks.isEmpty()) {
            // This cannot happen for this simple agent, but typically
            // you will need to consider the carriedTasks when the next
            // plan is computed.
        }
    }
}
