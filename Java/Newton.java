/*
 * Author: Karley Herschelman, kherschelman2012@my.fit.edu
 * Course: CSE 1002, Section 01/02, Fall 2013
 * Project: newtonchaos
 */
public class Newton {

    private static final double RANGE = .001;

    static class Complex {
        private static final int ONE_HUNDRED = 100;
        private static final int THREE = 3;
        private static final int FOUR = 4;
        private final double re; // the real part
        private final double im; // the imaginary part

        public final Complex root () {
            Complex coordinates = new Complex(re, im);
            for (int i = 0; i < ONE_HUNDRED; i++) {
                // numerator is f(z)
                final Complex numerator = coordinates.power(FOUR).minus(
                        new Complex(1, 0)); // zero imaginary
                // Denominator is derivative of f(z)
                final Complex denominator = coordinates.power(THREE).times(
                        new Complex(FOUR, 0));
                // saves value as new coordinate to iterate and return
                coordinates = coordinates.minus(numerator.divides(denominator));
            }
            return coordinates;
        }

        public Complex(final double real, final double imag) {
            re = real;
            im = imag;
        }

        public final Complex minus (final Complex b) {
            final double real = re - b.re;
            final double imag = im - b.im;
            return new Complex(real, imag);
        }

        public final Complex divides (final Complex b) {
            final double d = b.re * b.re + b.im * b.im;
            final double real = (re * b.re + im * b.im) / d;
            final double imag = (b.re * im - re * b.im) / d;
            return new Complex(real, imag);
        }

        public final Complex power (final int n) {
            Complex power = new Complex(re, im);
            final Complex value = new Complex(re, im);
            for (int i = 1; i < n; i++) {
                power = power.times(value);
            }
            return power;
        }

        public final Complex times (final Complex b) {
            final double real = re * b.re - im * b.im;
            final double imag = re * b.im + im * b.re;
            return new Complex(real, imag);
        }

        public final double abs () { // Math.sqrt(re*re + im*im)
            return Math.hypot(re, im);
        }

    }

    public static void main (final String[] args) {
        final int num = Integer.parseInt(args[0]);
        StdDraw.show(0); // turns off show
        StdDraw.setXscale(-1, 1);
        StdDraw.setYscale(-1, 1);
        // far left/bottom of square is -1,-1, iterates through all points to
        // far right/top (1,1) by adding width of 1 / # pixels
        for (double x = -1; x <= 1; x = x + 1.0 / num) {
            for (double y = -1; y <= 1; y += 1.0 / num) {
                point(x, y);
            }
        }
        StdDraw.show(); // shows points
    }

    public static void point (final double x, final double y) {
        final Complex coordinates = new Complex(x, y);
        final Complex root = coordinates.root();
        // absolute value checks radius around given point to see if it is in
        // range
        if (root.minus(new Complex(1, 0)).abs() <= RANGE) {
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.point(x, y);
        } else if (root.minus(new Complex(-1, 0)).abs() <= RANGE) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.point(x, y);
        } else if (root.minus(new Complex(0, 1)).abs() <= RANGE) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.point(x, y);
        } else if (root.minus(new Complex(0, -1)).abs() <= RANGE) {
            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.point(x, y);
        } else {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(x, y);
        }
    }
}
