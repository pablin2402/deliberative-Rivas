package deliberative.template.rivas;

import logist.agent.Agent;
import logist.behavior.DeliberativeBehavior;
import logist.plan.Action;
import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.TaskDistribution;
import logist.task.TaskSet;
import logist.topology.Topology;

import java.util.List;

public class RivasDeliberative implements DeliberativeBehavior {
    enum Algorithm {
        BFS, ASTAR
    }

    Topology topology;
    TaskDistribution td;
    Agent agent;
    Algorithm algorithm;

    @Override
    public void setup(Topology topology, TaskDistribution td, Agent agent) {
        this.topology = topology;
        this.td = td;
        this.agent = agent;
        String algorithmName = agent.readProperty("algorithm", String.class, "ASTAR");
        algorithm = Algorithm.valueOf(algorithmName.toUpperCase());
    }

    @Override
    public Plan plan(Vehicle vehicle, TaskSet tasks) {
        State state = null;
        BFS bfs = new BFS();
        AStar aStar = new AStar();
        switch (algorithm) {
            case ASTAR:
                state = aStar.aStarAlgorithm(vehicle, tasks);
                break;
            case BFS:
                state = bfs.getBreathFirstSearch(vehicle, tasks);
                break;
            default:
                throw new AssertionError("Should not happen.");
        }
        List<Action> actions = null;
        if (state != null) {
            actions = state.getCurrentActions();
        }
        return new Plan(vehicle.getCurrentCity(), actions);
    }

    @Override
    public void planCancelled(TaskSet carriedTasks) {
    }
}
