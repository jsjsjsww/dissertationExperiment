# Constrained Covering Array Generation

This project aims to investigate the impact of different constraint handlers on the execution cost-effectinveness of covering array generation algorithms. Specifically, we implement the following three representative covering array generation algorithms (frameworks):

1. *AETG* (the one-test-at-a-time framework) [1]
2. *IPO* (the in-parameter-order framework) [2]
3. *SA* (the evolve-test-suite framework) [3]

and the following four constraint handlers:

1. *Verify*: maintain a list of forbidden tuples, and each partial or full solution during the generation process will be verified against them to prevent the appearance of constraint violation.
2. *Solver*: encode constraints and solutions into a formula and apply an existing constraint satisfaction solver to check the formula's validity.
3. *Tolerate*: incorporate a penalty term into the fitness function to include invalid solutions in the search space, but penalise them in favour of valid solutions (can only be combined with *SA*).
4. *Replace*: generate a covering array without considering constraints, and then resolve conflicts by replacing invalid test cases by a set of valid ones to remove constraint violations while retaining combination coverage 

## Experimental Data

The experiment is conducted on a well-known benchmark of constrained covering array generation. This benchmark consists of 35 test models, each of which has a model file `name.model` and a constraint file `name.constraints`. The formats of these two files are the same as the inputs of [CASA](https://cse.unl.edu/~citportal/citportal/loadTool?page=casa).

The raw data is given in the `/data` directory of this project, where each plain text file recoreds the results for a test model (every varaint of AETG and SA is repeated 30 times, while the determinstic IPO is only executed once). The format of each data file is as follows:

```
apache         # name of the test model
2-way
Verify         # constraint handler
42 32.427S     # obtained test suite size and execution cost in each run 
44 23.12S
...
Solver
42 2M50.126S
46 3M15.061S
...
Replace
39 19.374S
37 19.109S
...
```


## Usage

To generate a constraind 2-way covering array by a given algorithm and a given constraint handler, run

`java -jar CCAG.jar`

and then follow the instructions displayed in the console.

## Reference

[1] M. B. Cohen, M. B. Dwyer, and J. Shi, “Constructing interaction test suites for highly-configurable systems in the presence of constraints: A greedy approach,” IEEE Transactions on Software Engineering, vol. 34, no. 5, pp. 633–650, 2008.

[2] Y. Lei, R. N. Kacker, D. R. Kuhn, V. Okun, and J. Lawrence, “IPOG/IPOG-D: efficient test generation for multi-way combinatorial testing,” Software Testing, Verification and Reliability, vol. 18, no. 3, pp. 125–148, 2008.

[3] B. J. Garvin, M. B. Cohen, and M. B. Dwyer, “Evaluating improvements to a meta-heuristic search for constrained interaction testing,” Empirical Software Engineering, vol. 16, no. 1, pp. 61–102, 2010.
