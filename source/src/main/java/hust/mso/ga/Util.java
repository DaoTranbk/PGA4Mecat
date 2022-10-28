package hust.mso.ga;

public class Util {
    public static double EPSILON = 0; // 1e-8;

    // for sgde
    public static int[] prefix_sum;
    public static void init_prefix_sum(int size) {
        prefix_sum = new int[size];
        prefix_sum[0] = 0;
        for (int i = 1; i < size; i++) {
            prefix_sum[i] = prefix_sum[i - 1] + i;
        }
    }

    public static boolean out_bound(double value, double lower_bound, double upper_bound) {
        return value < lower_bound || value > upper_bound;
    }

    public static double repair_sol_in_bound(double value, double lower_bound, double upper_bound) {
        double result = value;
        if (result < lower_bound)
            result = lower_bound + EPSILON;
        if (result > upper_bound)
            result = upper_bound - EPSILON;

        return result;
    }

    public static double repair_sol_with_random_value(double value, double lower_bound, double upper_bound) {
        double result = value;

        double delta = 0;
        if (value < lower_bound) {
            delta = lower_bound - value;
            delta = delta - Math.floor(delta);
            result = upper_bound - delta;
        }

        if (value > upper_bound) {
            delta = value - upper_bound;
            delta = delta - Math.floor(delta);
            result = delta - lower_bound;
        }

        return result;
    }

    public static double spearman(double[] x, double[] y) {
        double Rs;

        double n = x.length;
        double sum2 = 0;
        double delta;
        for (int i = 0; i < n; i++) {
            delta = x[i] - y[i];
            sum2 += delta * delta;
        }

        Rs = 1 - (6 * sum2) / (n * (n * n - 1));
        return Rs;
    }

    public static double mean(double[] arr) {
        int size = arr.length;
        double sum = 0;
        for (double i : arr) {
            sum += i;
        }

        return sum / size;
    }

    // x, y must has same length
    public static double cov(double[] x, double[] y) {
        double mean_x = mean(x);
        double mean_y = mean(y);

        double covariance = 0;
        int size = x.length;
        for (int i = 0; i < size; i++) {
            covariance += (x[i] - mean_x) * (y[i] - mean_y);
        }

        return covariance;
    }

    public static double std(double[] x) {
        return Math.sqrt(cov(x, x));
    }

    public static int argmin(double[] arr) {
        int min_id = 0;
        double min = Parameter.INF;

        for (int i = 0; i < arr.length; i++) {
            if (min > arr[i]) {
                min = arr[i];
                min_id = i;
            }
        }

        return min_id;
    }

    public static int argmax(double[] arr) {
        int max_id = 0;
        double max = -Parameter.INF;

        for (int i = 0; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
                max_id = i;
            }
        }

        return max_id;
    }

    public static int argmax(int[] arr) {
        int max_id = 0;
        int max = -Parameter.INF;

        for (int i = 0; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
                max_id = i;
            }
        }

        return max_id;
    }

    public static boolean is_single_value_arr(double[] arr) {
        double tmp = arr[0];
        for (double i : arr) {
            if (tmp != i)
                return false;

        }

        return true;
    }

    public static double erf_inverse(double u) {
        double alpha = 8 * (Math.PI - 3) / (3 * Math.PI * (4 - Math.PI));
        double a = 2 / (Math.PI * alpha) + Math.log(1 - u * u) / 2;
        double b = Math.log(1 - u * u) / alpha;
        double expr = Math.sqrt(Math.pow(a, 2) - b) - a;
        double rs = Math.signum(u) * Math.sqrt(expr);
        return rs;
    }

    // fractional error less than x.xx * 10 ^ -4.
    // Algorithm 26.2.17 in Abromowitz and Stegun, Handbook of Mathematical.
    public static double erf(double z) {
        double t = 1.0 / (1.0 + 0.5 * Math.abs(z));

        // use Horner's method
        double ans = 1 - t * Math.exp(-z * z - 1.26551223
                + t * (1.00002368 + t * (0.37409196 + t * (0.09678418 + t * (-0.18628806 + t * (0.27886807
                        + t * (-1.13520398 + t * (1.48851587 + t * (-0.82215223 + t * (0.17087277))))))))));
        if (z >= 0)
            return ans;
        else
            return -ans;
    }
}
