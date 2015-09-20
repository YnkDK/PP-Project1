# Pervasive Positioning - Project 1

We use `ant` to compile and run this project, use:

    Execution Targets:
        model:          Runs the model data for a given k (default:3) and num_runs (default:100)
        model.all:      Runs the model data for k in [1..5] and generates the needed plots
        empirical:      Runs the empirical data for a given k (default:3) and num_runs (default:100)
        empirical.all:  Runs the empirical data for k in [1..5] and generates the needed plots

e.g.. `ant model.all` generates the following files:

1. The data file `output/model_FP_NN`, i.e. raw data
2. The PDF file `output/model_FP_NN_k1.pdf`, i.e. the cumulative distribution for error distance with k = 1
2. The PDF file `output/model_FP_NN_k3.pdf`, i.e. the cumulative distribution for error distance with k = 3
2. The PDF file `output/model_FP_NN_median_acc.pdf`, i.e. the median accuracy for 1 <= k <= 5

The two parameters can be altered, e.g. `ant empirical -Dk=9 -Dnum_runs=42` uses k = 9 and repeats the experiment 42 times.

## Dependencies

1. Java 7 without any external dependencies
2. Rscript 2.14.1 (or greater) without any external dependencies
3. ant 1.8.2 (or greater) without any external dependencies