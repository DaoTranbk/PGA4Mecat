package hust.mso.ga.Heuristic;

public class Utils {
    // Union-Find with union by rank and path compression
    public static void make_set(int[] root, int[] rank, int u) {
        root[u] = u;
        rank[u] = 0;
    }

    public static int find(int[] root, int u) {
        if (root[u] != u) {
            root[u] = find(root, root[u]);
        }

        return root[u];
    }

    public static void union(int[] root, int[] rank, int u, int v) {
        int r1 = root[u];
        int r2 = root[v];

        if (r1 != r2) {
            if (rank[r1] > rank[r2]) {
                root[r2] = r1;
            } else {
                root[r1] = r2;
                if (rank[r1] == rank[r2]) {
                    rank[r2] = rank[r2] + 1;
                }
            }
        }
    }
}
