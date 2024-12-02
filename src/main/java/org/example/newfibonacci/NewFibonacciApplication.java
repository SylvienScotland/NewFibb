package org.example.newfibonacci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class NewFibonacciApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewFibonacciApplication.class, args);
    }
}

@RestController
@RequestMapping("/api")
class FibonacciController {

    /**
     * Computes the nth term in the Fibonacci sequence using an iterative approach.
     *
     * @param n the index of the term in the Fibonacci sequence to compute
     * @return a descriptive response with the nth term in the Fibonacci sequence
     */
    @GetMapping("/fibonacci")
    public String calculateFibonacci(@RequestParam int n) {
        if (n < 0) {
            return "Invalid input: n must be non-negative.";
        }
        if (n <= 1) {
            return "The " + n + "th term of the Fibonacci sequence is " + n;
        }

        int prev1 = 0, prev2 = 1, current = 1;
        for (int i = 2; i <= n; i++) {
            current = prev1 + prev2;
            prev1 = prev2;
            prev2 = current;
        }

        return "The " + n + "th term of the Fibonacci sequence is " + current;
    }
}
