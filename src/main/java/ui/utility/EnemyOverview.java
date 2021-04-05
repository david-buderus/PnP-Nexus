package ui.utility;

import ui.part.GraphView;

public class EnemyOverview extends GraphView<String, String> {

    public EnemyOverview() {
        super();
        this.setPrefSize(500, 500);
        getGraph().addNode("Test");
        getGraph().addNode("Test1");
        getGraph().addEdge("Test", "Test1");
        getGraph().addNode("Test2");
        getGraph().addNode("Test3");
        getGraph().addNode("Test4");
    }
}
