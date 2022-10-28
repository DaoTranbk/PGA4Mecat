package hust.mso.ga;

public class Node implements Comparable<Node> {
    double x, y, r;
    public double init_energy;

    int recv_packets = 0;
    public int id;
    public int report_size;
    public int aggregation_report_size;

    // for DA Tree
    public double key; // weight's edge which connects between this node and its parent
    public int parent;

    public Node(int id) {
        this.id = id;
    }

    public Node(int id, double key) {
        this.id = id;
        this.key = key;
    }

    public Node(int id, double x, double y, double r, double init_energy, int report_size) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.init_energy = init_energy;
        this.report_size = report_size;
        this.aggregation_report_size = report_size;
    }

    public Node(Node node) {
        this.id = node.id;
        this.init_energy = node.init_energy;
        this.key = node.key;
        this.parent = node.parent;
        this.report_size = node.report_size;
        this.aggregation_report_size = node.aggregation_report_size;
        this.recv_packets = node.recv_packets;
    }

    public void recv_report_size(int data_size) {
        recv_packets += (int) Math.ceil((double) data_size / Task.q);
        aggregation_report_size += data_size;
    }

    public double get_energy_consumption() {
        int send_packets = (int) Math.ceil((double) aggregation_report_size / Task.q);
        return Task.Tx * send_packets + Task.Rx * recv_packets;
    }

    public double energy_comsumption() {
        int no_packets = (int) Math.ceil((double) aggregation_report_size / Task.q);
        return (Task.Tx + Task.Rx) * no_packets;
    }

    public double get_energy_left() {
        double energy_consumption = get_energy_consumption();
        if (init_energy > energy_consumption)
            return init_energy - get_energy_consumption();
        else
            return Parameter.INF;
    }

    @Override
    public int compareTo(Node other) {
        if (key == other.key)
            return 0;
        return key > other.key ? -1 : 1;
    }

    @Override
    public String toString() {
        return String.format("id: %d, key: %f, parent: %d", this.id, this.key, this.parent);
    }
}
